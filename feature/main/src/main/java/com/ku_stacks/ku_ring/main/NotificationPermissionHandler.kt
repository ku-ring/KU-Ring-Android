package com.ku_stacks.ku_ring.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.util.checkHasNotificationPermission

@Composable
fun NotificationPermissionHandler(
    onPermissionGranted: () -> Unit,
    canShowSettingsDialog: () -> Boolean,
    onSettingsDialogShown: () -> Unit,
    openAppNotificationSettings: () -> Unit = {},
) {
    val context = LocalContext.current
    var isOpenSettingsDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isAppSettingsOpened by rememberSaveable { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        if (context.checkHasNotificationPermission() && isAppSettingsOpened) {
            onPermissionGranted()
            isAppSettingsOpened = false
        }
        onPauseOrDispose {}
    }

    RequestNotificationPermission(
        onGranted = onPermissionGranted,
        onDenied = {
            if (canShowSettingsDialog()) {
                isOpenSettingsDialogVisible = true
            }
        },
    )

    if (isOpenSettingsDialogVisible) {
        KuringAlertDialog(
            text = stringResource(R.string.notification_permission_dialog_text),
            confirmText = stringResource(R.string.notification_permission_dialog_confirm),
            onConfirm = {
                isAppSettingsOpened = true
                isOpenSettingsDialogVisible = false
                onSettingsDialogShown()
                openAppNotificationSettings()
            },
            onCancel = {
                isOpenSettingsDialogVisible = false
                onSettingsDialogShown()
            },
        )
    }
}

@Composable
private fun RequestNotificationPermission(
    onGranted: () -> Unit = {},
    onDenied: () -> Unit = {},
) {
    val context = LocalContext.current
    if (!context.checkHasNotificationPermission()) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) onGranted() else onDenied()
        }

        LaunchedEffect(Unit) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
