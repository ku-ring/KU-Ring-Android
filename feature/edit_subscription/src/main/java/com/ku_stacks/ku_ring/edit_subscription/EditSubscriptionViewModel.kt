package com.ku_stacks.ku_ring.edit_subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.edit_subscription.uimodel.DepartmentSubscriptionUiModel
import com.ku_stacks.ku_ring.edit_subscription.uimodel.NormalSubscriptionUiModel
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.util.modifyList
import com.ku_stacks.ku_ring.util.modifyMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditSubscriptionViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
    private val noticeRepository: NoticeRepository,
    private val analytics: EventAnalytics,
    private val preferenceUtil: PreferenceUtil,
    firebaseMessaging: FirebaseMessaging
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val initialCategories = MutableStateFlow(emptyList<NormalSubscriptionUiModel>())
    private val initialDepartments = MutableStateFlow(emptyList<Department>())

    private val categories = MutableStateFlow(emptyList<NormalSubscriptionUiModel>())
    val sortedCategories: Flow<List<NormalSubscriptionUiModel>> = categories

    private val departmentsByKoreanName = MutableStateFlow(mutableMapOf<String, Department>())

    val sortedDepartments: Flow<List<DepartmentSubscriptionUiModel>> =
        departmentsByKoreanName.map { departments ->
            departments.values.map { it.toSubscriptionUiModel() }.sortedWith(DepartmentComparator)
        }

    val hasUpdate: StateFlow<Boolean> =
        combine(
            categories,
            departmentsByKoreanName,
            initialCategories,
            initialDepartments
        ) { categories, departments, initialCategories, initialDepartments ->
            (categories.toSet() != initialCategories) || (departments.values.toSet() != initialDepartments)
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private var fcmToken: String? = null

    /** 초기 설정이 끝나기 전에 뒤로가기를 하면 빈 목록을 구독하는 경우를 방지하기 위함 */
    private var isInitialCategoryLoaded = false
    private var isInitialDepartmentLoaded = false
    val isInitialLoadDone: Boolean
        get() = isInitialCategoryLoaded && isInitialDepartmentLoaded

    /** 첫 앱 구동자에게 보여지는 온보딩 후의 푸시 세팅을 위한 분기처리 용도 */
    var firstRunFlag = false

    init {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("Firebase instanceId fail : ${task.exception}")
                analytics.errorEvent("${task.exception}", className)
            } else if (task.result == null) {
                Timber.e("Fcm Token is null")
                analytics.errorEvent("Fcm Token is null!", className)
            } else {
                fcmToken = task.result
                syncWithServer()
            }
        }
    }

    fun syncWithServer() {
        fcmToken?.let {
            disposable.add(
                noticeRepository.fetchSubscriptionFromRemote(fcmToken!!)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        initialSortSubscription(it)
                    }, {
                        Timber.e("getSubscribeList fail $it")
                    })
            )
        }

        viewModelScope.launch {
            departmentRepository.updateDepartmentsFromRemote()
            val subscribedDepartments = departmentRepository.getSubscribedDepartments()
            addDepartmentsToMap(subscribedDepartments)
            val notificationEnabledDepartments = departmentRepository.fetchSubscribedDepartments()
            markDepartmentsAsEnabled(notificationEnabledDepartments)
            isInitialDepartmentLoaded = true
            initialDepartments.modifyList { addAll(departmentsByKoreanName.value.values) }
        }
    }

    private fun addDepartmentsToMap(departments: List<Department>) {
        departmentsByKoreanName.modifyMap {
            departments.forEach {
                this[it.koreanName] = it
            }
        }
    }

    fun saveSubscribe() {
        if (!isInitialLoadDone) {
            Timber.d("return because category: $isInitialCategoryLoaded, department: $isInitialDepartmentLoaded")
            return
        }

        fcmToken?.let { fcmToken ->
            val notificationEnabledCategories =
                categories.value.filter { it.isSelected }.map { it.categoryName }
            preferenceUtil.saveSubscriptionFromKorean(notificationEnabledCategories)
            noticeRepository.saveSubscriptionToRemote(
                token = fcmToken,
                subscribeCategories = notificationEnabledCategories.map { category ->
                    WordConverter.convertKoreanToEnglish(category)
                }
            )
        }
        viewModelScope.launch {
            val subscribedDepartments =
                departmentsByKoreanName.value.values.filter { it.isSelected }
            departmentRepository.saveSubscribedDepartmentsToRemote(subscribedDepartments)
        }
    }

    private fun markDepartmentsAsEnabled(departments: List<Department>) {
        Timber.d("Mark: add $departments to $departmentsByKoreanName")
        departmentsByKoreanName.modifyMap {
            departments.forEach {
                this[it.koreanName] = this[it.koreanName]!!.copy(isSelected = true)
            }
        }
    }

    fun onItemClick(item: SubscriptionUiModel) {
        val content = item.content
        if (content in departmentsByKoreanName.value) {
            // department
            departmentsByKoreanName.modifyMap {
                this[content] = this[content]!!.toggle()
            }
        } else {
            // category
//            categories.modifyMap {
//                this[content] = this[content]!!.toggle()
//            }
        }
    }

    fun onNormalSubscriptionItemClick(index: Int) {
        categories.modifyList {
            this[index] = this[index].toggle()
        }
    }

    fun rollback() {
        categories.modifyList {
            this.clear()
            initialCategories.value.forEach {
                this.add(it)
            }
        }

        departmentsByKoreanName.modifyMap {
            this.clear()
            initialDepartments.value.forEach {
                this[it.koreanName] = it
            }
        }
    }

    private fun initialSortSubscription(subscribingList: List<String>) {
        val subscribingSet = subscribingList.toSet()
        val initialNormalSubscriptionItems = NormalSubscriptionUiModel.initialValues.map {
            if (it.categoryName in subscribingSet) {
                it.toggle()
            } else {
                it
            }
        }
        categories.modifyList { addAll(initialNormalSubscriptionItems) }
        initialCategories.modifyList { addAll(initialNormalSubscriptionItems) }
        isInitialCategoryLoaded = true
    }

    object DepartmentComparator : Comparator<DepartmentSubscriptionUiModel> {
        override fun compare(
            p0: DepartmentSubscriptionUiModel,
            p1: DepartmentSubscriptionUiModel
        ): Int {
            return p0.name.compareTo(p1.name)
        }
    }

    private fun Department.toSubscriptionUiModel() = DepartmentSubscriptionUiModel(
        name = koreanName,
        isSelected = isNotificationEnabled
    )

    private fun Department.toggle() =
        Department(name, shortName, koreanName, isSubscribed, isSelected, !isNotificationEnabled)

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    companion object {
        private val className: String = EditSubscriptionViewModel::class.java.simpleName
    }
}