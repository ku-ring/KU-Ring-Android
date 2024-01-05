package com.ku_stacks.ku_ring.ui_util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.WebViewNotice

interface KuringNavigator {
    fun createEditSubscriptionIntent(context: Context, isFirstRun: Boolean = false): Intent
    fun navigateToEditSubscription(activity: Activity, isFirstRun: Boolean = false)
    // TODO: 학과 편집하기 화면 구현 후 일반 Activity로 매개변수 타입 바꾸기
    fun navigateToEditSubscribedDepartment(fragmentActivity: FragmentActivity)
    fun navigateToFeedback(activity: Activity)
    fun createMainIntent(context: Context): Intent
    fun navigateToMain(activity: Activity)
    fun navigateToMain(activity: Activity, url: String, articleId: String, category: String)
    fun navigateToNotification(activity: Activity)
    fun navigateToNoticeStorage(activity: Activity)
    fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        category: String?
    ): Intent
    fun navigateToNoticeWeb(activity: Activity, notice: Notice)
    fun navigateToNoticeWeb(activity: Activity, webViewNotice: WebViewNotice)
    fun navigateToNoticeWeb(activity: Activity, url: String?, articleId: String?, category: String?)
    fun navigateToNotionView(activity: Activity, notionUrl: String)
    fun navigateToOnboarding(activity: Activity)
    fun navigateToSplash(activity: Activity)
    fun navigateToOssLicensesMenu(activity: Activity)
}