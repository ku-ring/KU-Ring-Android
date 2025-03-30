package com.ku_stacks.ku_ring.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.auth.AuthActivity
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.domain.mapper.toWebViewNotice
import com.ku_stacks.ku_ring.edit_departments.EditDepartmentsActivity
import com.ku_stacks.ku_ring.edit_subscription.EditSubscriptionActivity
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.kuringbot.KuringBotActivity
import com.ku_stacks.ku_ring.library.LibrarySeatActivity
import com.ku_stacks.ku_ring.main.MainActivity
import com.ku_stacks.ku_ring.main.search.SearchActivity
import com.ku_stacks.ku_ring.notice_detail.NoticeWebActivity
import com.ku_stacks.ku_ring.notion.NotionViewActivity
import com.ku_stacks.ku_ring.onboarding.OnboardingActivity
import com.ku_stacks.ku_ring.splash.SplashActivity
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import javax.inject.Inject

class KuringNavigatorImpl @Inject constructor() : KuringNavigator {
    override fun navigateToEditSubscription(activity: Activity) {
        EditSubscriptionActivity.start(activity)
    }

    override fun navigateToEditSubscribedDepartment(activity: Activity) {
        EditDepartmentsActivity.start(activity)
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
        category: String,
        subject: String,
    ) {
        MainActivity.start(activity, url, articleId, category, subject)
    }

    override fun navigateToSearch(activity: Activity) {
        SearchActivity.start(activity)
    }

    override fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        category: String?,
        subject: String?,
    ): Intent {
        return NoticeWebActivity.createIntent(context, url, articleId, category, subject)
    }

    override fun navigateToNoticeWeb(activity: Activity, notice: Notice) {
        NoticeWebActivity.start(activity, notice.toWebViewNotice())
    }

    override fun navigateToNoticeWeb(activity: Activity, webViewNotice: WebViewNotice) {
        NoticeWebActivity.start(activity, webViewNotice)
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

    override fun navigateToKuringBot(context: Context) {
        KuringBotActivity.start(context)
    }

    override fun navigateToLibrarySeat(activity: Activity) {
        LibrarySeatActivity.start(activity)
    }

    override fun navigateToAuth(context: Context) {
        AuthActivity.start(context)
    }
}
