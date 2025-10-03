package com.ku_stacks.ku_ring.main.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.academicevent.usecase.GetDistinctAcademicEventUseCase
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentNoticeViewModel @Inject constructor(
    private val preferenceUtil: PreferenceUtil,
    private val noticeRepository: NoticeRepository,
    private val departmentRepository: DepartmentRepository,
    private val getDistinctAcademicEventUseCase: GetDistinctAcademicEventUseCase,
) : ViewModel() {

    private val _subscribedDepartments = MutableStateFlow(emptyList<Department>())
    val subscribedDepartments: StateFlow<List<Department>>
        get() = _subscribedDepartments
    private val _academicEvents = MutableStateFlow<List<AcademicEvent>>(emptyList())
    val academicEvents: StateFlow<List<AcademicEvent>> = _academicEvents.asStateFlow()

    private val isInitialLoading = MutableStateFlow(true)

    val departmentNoticeScreenState: StateFlow<DepartmentNoticeScreenState> = combine(
        isInitialLoading,
        subscribedDepartments,
    ) { isInitialLoading, subscribedDepartments ->
        when {
            isInitialLoading -> DepartmentNoticeScreenState.InitialLoading
            subscribedDepartments.isEmpty() -> DepartmentNoticeScreenState.DepartmentsEmpty
            else -> DepartmentNoticeScreenState.DepartmentsNotEmpty
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DepartmentNoticeScreenState.InitialLoading
    )

    val currentDepartmentNotice: StateFlow<Flow<PagingData<Notice>>?> =
        subscribedDepartments.map { departments ->
            val selectedDepartment = departments.firstOrNull { it.isSelected }
            if (selectedDepartment == null) {
                null
            } else {
                noticeRepository.getDepartmentNotices(selectedDepartment.shortName)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        collectSubscribedDepartments()
    }

    private fun collectSubscribedDepartments() {
        viewModelScope.launch {
            departmentRepository.getSubscribedDepartmentsAsFlow().collectLatest { departments ->
                _subscribedDepartments.value = departments
                isInitialLoading.value = false
            }
        }
    }

    fun selectDepartment(department: Department) {
        viewModelScope.launch {
            departmentRepository.clearMainDepartments()
            departmentRepository.updateMainDepartmentStatus(department.name, true)
        }
    }

    suspend fun fetchAcademicEvents() {
        val (startDate, endDate) = with(LocalDate.now()) {
            val dayOfWeek = this.dayOfWeek.ordinal
            val startDate = this.minus(dayOfWeek, DateTimeUnit.DAY)
            val endDate = startDate.plus(6, DateTimeUnit.DAY)
            startDate to endDate
        }
        val events = getDistinctAcademicEventUseCase(startDate, endDate)
            .getOrElse { t ->
                Timber.e(t)
                emptyList()
            }
        _academicEvents.update { events }
    }

    fun shouldShowAcademicEventSheet(): Boolean {
        val isEventAvailable = academicEvents.value.isNotEmpty()
        val isEventSheetNotShown =
            preferenceUtil.lastDateAcademicEventShown != LocalDate.now().toString()
        return isEventAvailable && isEventSheetNotShown
    }

    fun markAcademicEventSheetAsShown() {
        preferenceUtil.lastDateAcademicEventShown = LocalDate.now().toString()
    }
}

sealed class DepartmentNoticeScreenState {
    object InitialLoading : DepartmentNoticeScreenState()
    object DepartmentsEmpty : DepartmentNoticeScreenState()
    object DepartmentsNotEmpty : DepartmentNoticeScreenState()
}