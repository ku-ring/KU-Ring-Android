package com.ku_stacks.ku_ring.notification.compose.innerscreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.notification.NotificationViewModel
import com.ku_stacks.ku_ring.notification.compose.component.NotificationTopBar
import com.ku_stacks.ku_ring.notification.compose.component.SwipeToRevealDeleteBox
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
    val revealedItemId by viewModel.revealedItemId.collectAsStateWithLifecycle()

    NotificationScreen(
        notificationUiModels = notificationUiModels,
        revealedItemId = revealedItemId,
        onNavigationClick = onNavigateUp,
        onEditSubscriptionClick = onNavigateToEditSubscription,
        onNotificationClick = { notification ->
            onNotificationClick(notification)
            viewModel.updateNotificationAsRead(notification.id)
            viewModel.setRevealedItemId(null)
        },
        onNotificationDelete = viewModel::deleteNotification,
        onNotificationReveal = viewModel::setRevealedItemId,
    )
}

@Composable
private fun NotificationScreen(
    notificationUiModels: LazyPagingItems<NotificationUiModel>,
    revealedItemId: Int?,
    onNavigationClick: () -> Unit,
    onEditSubscriptionClick: () -> Unit,
    onNotificationClick: (Notification) -> Unit,
    onNotificationDelete: (Int) -> Unit,
    onNotificationReveal: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            NotificationTopBar(
                onNavigationClick = onNavigationClick,
                onEditSubscriptionClick = onEditSubscriptionClick,
            )
        },
        containerColor = KuringTheme.colors.background,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { onNotificationReveal(null) }
            },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            items(
                count = notificationUiModels.itemCount,
                key = notificationUiModels.itemKey { it.notification.id },
                contentType = notificationUiModels.itemContentType { it.javaClass }
            ) { index ->
                notificationUiModels[index]?.let { uiModel ->
                    val notification = uiModel.notification
                    val isRevealed = revealedItemId == uiModel.notification.id

                    SwipeToRevealDeleteBox(
                        notificationUiModel = uiModel,
                        isRevealed = isRevealed,
                        onClick = { onNotificationClick(notification) },
                        onReveal = { onNotificationReveal(notification.id) },
                        onDelete = { onNotificationDelete(notification.id) },
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
            revealedItemId = null,
            notificationUiModels = notificationPagingData,
            onNavigationClick = {},
            onNotificationClick = {},
            onEditSubscriptionClick = {},
            onNotificationDelete = {},
            onNotificationReveal = {},
        )
    }
}
