package com.ku_stacks.ku_ring.notification.compose.innerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.notification.NotificationViewModel
import com.ku_stacks.ku_ring.notification.compose.component.NotificationTopBar
import com.ku_stacks.ku_ring.notification.compose.component.SwipeToDeleteNotificationBox
import com.ku_stacks.ku_ring.notification.compose.preview.NotificationsPreviewParameterProvider
import com.ku_stacks.ku_ring.notification.model.NotificationUiModel
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun NotificationScreen(
    onNavigateUp: () -> Unit,
    onNavigateToEditSubscription: () -> Unit,
    onNotificationClick: (Notification) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notificationUiModels = viewModel.notificationsFlow.collectAsLazyPagingItems()

    NotificationScreen(
        notificationUiModels = notificationUiModels,
        onNavigationClick = onNavigateUp,
        onEditSubscriptionClick = onNavigateToEditSubscription,
        onNotificationClick = { notification ->
            onNotificationClick(notification)
            viewModel.updateNotificationAsRead(notification)
        },
        onDeleteNotification = viewModel::deleteNotification,
    )
}

@Composable
private fun NotificationScreen(
    notificationUiModels: LazyPagingItems<NotificationUiModel>,
    onNavigationClick: () -> Unit,
    onEditSubscriptionClick: () -> Unit,
    onNotificationClick: (Notification) -> Unit,
    onDeleteNotification: (Notification) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = KuringTheme.colors.background)
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        NotificationTopBar(
            onNavigationClick = onNavigationClick,
            onEditSubscriptionClick = onEditSubscriptionClick,
        )
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
        ) {
            items(
                count = notificationUiModels.itemCount,
                key = { index -> notificationUiModels[index]?.notification?.id ?: index },
                contentType = notificationUiModels.itemContentType { it.javaClass }
            ) { index ->
                notificationUiModels[index]?.let { uiModel ->
                    SwipeToDeleteNotificationBox(
                        notificationUiModel = uiModel,
                        onClick = { onNotificationClick(uiModel.notification) },
                        onDelete = { onDeleteNotification(uiModel.notification) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    )

                    if (index < notificationUiModels.itemCount - 1) {
                        HorizontalDivider(
                            thickness = Dp.Hairline,
                            color = KuringTheme.colors.borderline
                        )
                    }
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun NotificationScreenPreview(
    @PreviewParameter(NotificationsPreviewParameterProvider::class) notifications: List<Notification>
) {
    val notificationUiModels = notifications.map { NotificationUiModel(it) }
    val pagingData = PagingData.from(
        data = notificationUiModels,
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false)
        )
    )
    val notificationPagingData = flowOf(pagingData).collectAsLazyPagingItems()

    KuringTheme {
        NotificationScreen(
            notificationUiModels = notificationPagingData,
            onDeleteNotification = {},
            onNavigationClick = {},
            onNotificationClick = {},
            onEditSubscriptionClick = {},
        )
    }
}
