package com.ku_stacks.ku_ring.ui_util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.WebViewNotice

interface KuringNavigator {
    fun navigateToEditSubscription(activity: Activity)
    fun navigateToEditSubscribedDepartment(activity: Activity)
    fun navigateToFeedback(activity: Activity)
    fun createMainIntent(context: Context): Intent
    fun navigateToMain(activity: Activity)
    fun navigateToMain(activity: Activity, url: String, articleId: String, category: String, subject: String)
    fun navigateToSearch(activity: Activity)
    fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        category: String?,
        subject: String?,
    ): Intent
    fun navigateToNoticeWeb(activity: Activity, notice: Notice)
    fun navigateToNoticeWeb(activity: Activity, webViewNotice: WebViewNotice)
    fun navigateToNotionView(activity: Activity, notionUrl: String)
    fun navigateToOnboarding(activity: Activity)
    fun navigateToSplash(activity: Activity)
    fun navigateToOssLicensesMenu(activity: Activity)
    fun navigateToKuringBot(context: Context)
    fun navigateToLibrarySeat(activity: Activity)
    fun navigateToAuth(activity: Activity)
}
