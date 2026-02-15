package com.ku_stacks.ku_ring.notification.compose.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.ensureLineHeight
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_swipe_to_delete_box_delete
import com.ku_stacks.ku_ring.notification.compose.preview.NotificationPreviewParameterProvider
import com.ku_stacks.ku_ring.notification.model.NotificationUiModel

@Composable
fun SwipeToDeleteNotificationBox(
    notificationUiModel: NotificationUiModel,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromStartToEnd = false,
        modifier = modifier,
        onDismiss = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
        },
        backgroundContent = {
            BackgroundContent()
        },
    ) {
        ForegroundContent(
            notification = notificationUiModel.notification,
            content = notificationUiModel.content,
            icon = notificationUiModel.iconRes,
            categoryString = notificationUiModel.categoryStringRes,
            onClick = onClick,
        )
    }
}

@Composable
private fun ForegroundContent(
    notification: Notification,
    content: String,
    @DrawableRes icon: Int,
    @StringRes categoryString: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
) {
    val backgroundColor =
        if (notification.isNew) KuringTheme.colors.mainPrimarySelected
        else KuringTheme.colors.background
    val interaction = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(
                interactionSource = interaction,
                indication = ripple(),
                onClick = onClick,
            )
            .padding(contentPadding),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    tint = KuringTheme.colors.textCaption1,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(categoryString),
                    style = KuringTheme.typography.tag2.ensureLineHeight(),
                    color = KuringTheme.colors.textCaption1,
                )
            }
            Text(
                text = content,
                style = KuringTheme.typography.body1.ensureLineHeight(),
                color = KuringTheme.colors.textBody,
                maxLines = 1,
            )
        }

        Text(
            text = notification.receivedDate,
            style = KuringTheme.typography.caption1.ensureLineHeight(),
            color = KuringTheme.colors.textCaption1,
        )
    }
}

@Composable
private fun BackgroundContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.red)
            .padding(end = 18.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = stringResource(notification_swipe_to_delete_box_delete),
            style = KuringTheme.typography.caption1,
            color = KuringTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeToDeleteNotificationPreview(
    @PreviewParameter(NotificationPreviewParameterProvider::class) notification: Notification
) {
    SwipeToDeleteNotificationBox(
        notificationUiModel = NotificationUiModel(notification),
        onDelete = {},
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(5f)
    )
}
