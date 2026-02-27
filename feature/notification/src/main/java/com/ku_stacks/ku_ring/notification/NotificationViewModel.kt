package com.ku_stacks.ku_ring.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.notification.model.NotificationUiModel
import com.ku_stacks.ku_ring.notification.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    val notificationsFlow = getNotificationsAsFlow()
        .map { pagingData ->
            pagingData.map { NotificationUiModel(it) }
        }
        .cachedIn(viewModelScope)

    private fun getNotificationsAsFlow() = notificationRepository.getNotificationList()

    fun deleteNotification(notification: Notification) = viewModelScope.launch {
        notificationRepository.deleteNotification(notification.id)
    }

    fun updateNotificationAsRead(notification: Notification) = viewModelScope.launch {
        notificationRepository.updateNotificationAsRead(notification.id)
    }
}
