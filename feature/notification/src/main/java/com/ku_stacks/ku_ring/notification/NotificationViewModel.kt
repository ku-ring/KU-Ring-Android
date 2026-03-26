package com.ku_stacks.ku_ring.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.ku_stacks.ku_ring.notification.model.NotificationUiModel
import com.ku_stacks.ku_ring.notification.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _revealedItemId = MutableStateFlow<Int?>(null)
    val revealedItemId = _revealedItemId.asStateFlow()

    val notificationsFlow = getNotificationsAsFlow()
        .map { pagingData ->
            pagingData.map { NotificationUiModel(it) }
        }
        .cachedIn(viewModelScope)

    private var job: Job? = null

    private fun getNotificationsAsFlow() = notificationRepository.getNotificationList()

    fun deleteNotification(notificationId: Int) = viewModelScope.launch {
        notificationRepository.deleteNotification(notificationId)
    }

    fun updateNotificationAsRead(notificationId: Int) = viewModelScope.launch {
        notificationRepository.updateNotificationAsRead(notificationId)
    }

    fun setRevealedItemId(id: Int?) {
        job?.cancel()
        _revealedItemId.update { id }

        if (id != null) {
            job = viewModelScope.launch {
                delay(REVEAL_TIME_DELAY)
                _revealedItemId.update { null }
            }
        }
    }

    companion object {
        private const val REVEAL_TIME_DELAY = 3000L
    }
}
