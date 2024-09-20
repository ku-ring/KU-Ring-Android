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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSubscriptionViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
    private val noticeRepository: NoticeRepository,
    private val analytics: EventAnalytics,
    private val preferenceUtil: PreferenceUtil,
    firebaseMessaging: FirebaseMessaging
) : ViewModel() {
    private val initialCategories = MutableStateFlow(emptyList<NormalSubscriptionUiModel>())
    private val initialDepartments = MutableStateFlow(emptyList<Department>())

    private val categories = MutableStateFlow(emptyList<NormalSubscriptionUiModel>())
    private val departmentsByKoreanName = MutableStateFlow(mutableMapOf<String, Department>())

    val uiState: StateFlow<EditSubscriptionUiState> = combine(
        categories,
        departmentsByKoreanName,
    ) { categories, departmentsByKoreanName ->
        val sortedDepartments =
            departmentsByKoreanName.values.map { it.toSubscriptionUiModel() }.sortedWith(DepartmentComparator)
        EditSubscriptionUiState(
            categories = categories,
            departments = sortedDepartments,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        EditSubscriptionUiState.initialValue
    )

    private var fcmToken: String? = null

    /** 초기 설정이 끝나기 전에 뒤로가기를 하면 빈 목록을 구독하는 경우를 방지하기 위함 */
    private var isInitialCategoryLoaded = false
    private var isInitialDepartmentLoaded = false
    val isInitialLoadDone: Boolean
        get() = isInitialCategoryLoaded && isInitialDepartmentLoaded

    init {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                analytics.errorEvent(
                    "${task.exception}",
                    className
                )
            } else if (task.result == null) {
                analytics.errorEvent(
                    "Fcm Token is null!",
                    className
                )
            } else {
                fcmToken = task.result
                syncWithServer()
            }
        }
    }

    private fun syncWithServer() {
        fcmToken?.let {
            viewModelScope.launch {
                runCatching {
                    val subscribingList = noticeRepository.fetchSubscriptionFromRemote(it)
                    initialSortSubscription(subscribingList)
                }.onFailure {
                    analytics.errorEvent(
                        "${it.message}",
                        className
                    )
                }
            }
        }

        viewModelScope.launch {
            departmentRepository.updateDepartmentsFromRemote()
            collectSubscribedDepartments()
            fetchNotificationEnabledDepartments()
        }
    }

    private fun collectSubscribedDepartments() {
        viewModelScope.launch {
            departmentRepository.getSubscribedDepartmentsAsFlow().collectLatest { subscribedDepartments ->
                setDepartmentsToMap(subscribedDepartments)
            }
        }
    }

    private suspend fun fetchNotificationEnabledDepartments() {
        val notificationEnabledDepartments = departmentRepository.fetchSubscribedDepartments()
        markDepartmentsAsEnabled(notificationEnabledDepartments)
        initialDepartments.modifyList { addAll(departmentsByKoreanName.value.values) }
        isInitialDepartmentLoaded = true
    }

    private fun setDepartmentsToMap(departments: List<Department>) {
        departmentsByKoreanName.modifyMap {
            this.clear()
            departments.forEach {
                this[it.koreanName] = it
            }
        }
    }

    suspend fun saveSubscribe() {
        if (!isInitialLoadDone) {
            return
        }

        fcmToken?.let { fcmToken ->
            val notificationEnabledCategories = categories.value.filter { it.isSelected }.map { it.categoryName }
            preferenceUtil.saveSubscriptionFromKorean(notificationEnabledCategories)
            noticeRepository.saveSubscriptionToRemote(token = fcmToken,
                subscribeCategories = notificationEnabledCategories.map { category ->
                    WordConverter.convertKoreanToEnglish(category)
                })
        }
        viewModelScope.launch {
            val subscribedDepartments = departmentsByKoreanName.value.values.filter { it.isNotificationEnabled }
            departmentRepository.saveSubscribedDepartmentsToRemote(subscribedDepartments)
        }
    }

    private fun markDepartmentsAsEnabled(departments: List<Department>) {
        departmentsByKoreanName.modifyMap {
            departments.forEach {
                this[it.koreanName] = this[it.koreanName]!!.toggle()
            }
        }
    }

    fun onNormalSubscriptionItemClick(index: Int) {
        categories.modifyList {
            this[index] = this[index].toggle()
        }
    }

    fun onDepartmentSubscriptionItemClick(departmentName: String) {
        departmentsByKoreanName.modifyMap {
            try {
                this[departmentName] = this[departmentName]!!.toggle()
            } catch (e: NullPointerException) {
                e.printStackTrace()
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

    private fun Department.toggle() = Department(
        name,
        shortName,
        koreanName,
        isSubscribed,
        isSelected,
        !isNotificationEnabled
    )

    companion object {
        private val className: String = EditSubscriptionViewModel::class.java.simpleName
    }
}