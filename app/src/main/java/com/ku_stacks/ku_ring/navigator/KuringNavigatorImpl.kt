package com.ku_stacks.ku_ring.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.mapper.toWebViewNotice
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.edit_subscription.EditSubscriptionActivity
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.my_notification.NotificationActivity
import com.ku_stacks.ku_ring.notice_detail.NoticeWebActivity
import com.ku_stacks.ku_ring.notice_storage.NoticeStorageActivity
import com.ku_stacks.ku_ring.ui.chat.ChatActivity
import com.ku_stacks.ku_ring.ui.main.MainActivity
import com.ku_stacks.ku_ring.ui.notion.NotionViewActivity
import com.ku_stacks.ku_ring.ui.onboarding.OnboardingActivity
import com.ku_stacks.ku_ring.ui.splash.SplashActivity
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import javax.inject.Inject

class KuringNavigatorImpl @Inject constructor(): KuringNavigator {
    override fun navigateToChat(activity: Activity) {
        ChatActivity.start(activity)
    }

    override fun createEditSubscriptionIntent(context: Context, isFirstRun: Boolean): Intent {
        return Intent(context, EditSubscriptionActivity::class.java).apply {
            putExtra(EditSubscriptionActivity.FIRST_RUN_FLAG, isFirstRun)
        }
    }

    override fun navigateToEditSubscription(activity: Activity, isFirstRun: Boolean) {
        EditSubscriptionActivity.start(activity, isFirstRun)
    }

    override fun navigateToFeedback(activity: Activity) {
        FeedbackActivity.start(activity)
    }

    override fun createMainIntent(context: Context): Intent {
        return MainActivity.createIntent(context)
    }

    override fun navigateToMain(activity: Activity) {
        MainActivity.start(activity)
    }

    override fun navigateToMain(
        activity: Activity,
        url: String,
        articleId: String,
        category: String
    ) {
        MainActivity.start(activity, url, articleId, category)
    }

    override fun navigateToNotification(activity: Activity) {
        NotificationActivity.start(activity)
    }

    override fun navigateToNoticeStorage(activity: Activity) {
        NoticeStorageActivity.start(activity)
    }

    override fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        category: String?
    ): Intent {
        return NoticeWebActivity.createIntent(context, url, articleId, category)
    }

    override fun navigateToNoticeWeb(activity: Activity, notice: Notice) {
        NoticeWebActivity.start(activity, notice.toWebViewNotice())
    }

    override fun navigateToNoticeWeb(activity: Activity, webViewNotice: WebViewNotice) {
        NoticeWebActivity.start(activity, webViewNotice)
    }

    override fun navigateToNoticeWeb(
        activity: Activity,
        url: String?,
        articleId: String?,
        category: String?
    ) {
        NoticeWebActivity.start(activity, url, articleId, category)
    }

    override fun navigateToNotionView(activity: Activity, notionUrl: String) {
        NotionViewActivity.start(activity, notionUrl)
    }

    override fun navigateToOnboarding(activity: Activity) {
        OnboardingActivity.start(activity)
    }

    override fun navigateToSplash(activity: Activity) {
        SplashActivity.start(activity)
    }

    override fun navigateToOssLicensesMenu(activity: Activity) {
        val intent = Intent(activity, OssLicensesMenuActivity::class.java)
        activity.startActivity(intent)
        OssLicensesMenuActivity.setActivityTitle(activity.getString(R.string.open_source_license))
    }
}