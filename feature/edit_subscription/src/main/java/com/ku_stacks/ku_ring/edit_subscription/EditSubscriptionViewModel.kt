package com.ku_stacks.ku_ring.edit_subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.util.modifyMap
import com.ku_stacks.ku_ring.util.modifySet
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

    private val initialCategories = MutableStateFlow(mutableSetOf<SubscriptionUiModel>())
    private val initialDepartments = MutableStateFlow(mutableSetOf<Department>())

    private val categories = MutableStateFlow(mutableMapOf<String, SubscriptionUiModel>())
    val sortedCategories: Flow<List<SubscriptionUiModel>> =
        categories.map { it.values.sortedWith(CategoryComparator) }

    private val departmentsByKoreanName = MutableStateFlow(mutableMapOf<String, Department>())

    val sortedDepartments: Flow<List<SubscriptionUiModel>> =
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
            (categories.values.toSet() != initialCategories) || (departments.values.toSet() != initialDepartments)
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
            initialDepartments.modifySet { addAll(departmentsByKoreanName.value.values) }
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
                categories.value.values.filter { it.isNotificationEnabled }.map { it.content }
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
                departmentsByKoreanName.value.values.filter { it.isNotificationEnabled }
            departmentRepository.saveSubscribedDepartmentsToRemote(subscribedDepartments)
        }
    }

    private fun markDepartmentsAsEnabled(departments: List<Department>) {
        Timber.d("Mark: add $departments to $departmentsByKoreanName")
        departmentsByKoreanName.modifyMap {
            departments.forEach {
                this[it.koreanName] = this[it.koreanName]!!.copy(isNotificationEnabled = true)
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
            categories.modifyMap {
                this[content] = this[content]!!.toggle()
            }
        }
    }

    fun rollback() {
        categories.modifyMap {
            this.clear()
            initialCategories.value.forEach {
                this[it.content] = it
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
        categories.modifyMap {
            listOf("학사", "장학", "취창업", "국제", "학생", "산학", "일반", "도서관").forEach {
                this[it] = SubscriptionUiModel(it, it in subscribingSet)
            }
        }
        initialCategories.modifySet { addAll(categories.value.values) }
        isInitialCategoryLoaded = true
    }

    /**
     * 변경될때마다 정렬하는 방식 채택
     * getPriority()가 낮을수록 앞쪽으로 정렬
     */
    object CategoryComparator : Comparator<SubscriptionUiModel> {
        private fun getPriority(category: String): Int {
            return when (category) {
                "학사" -> 1
                "장학" -> 2
                "취창업" -> 3
                "국제" -> 4
                "학생" -> 5
                "산학" -> 6
                "일반" -> 7
                "도서관" -> 8
                else -> 100
            }
        }

        override fun compare(a: SubscriptionUiModel, b: SubscriptionUiModel): Int {
            return getPriority(a.content) - getPriority(b.content)
        }
    }

    object DepartmentComparator : Comparator<SubscriptionUiModel> {
        override fun compare(p0: SubscriptionUiModel, p1: SubscriptionUiModel): Int {
            return p0.content.compareTo(p1.content)
        }
    }

    private fun Department.toSubscriptionUiModel() = SubscriptionUiModel(
        content = koreanName,
        isNotificationEnabled = isNotificationEnabled
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