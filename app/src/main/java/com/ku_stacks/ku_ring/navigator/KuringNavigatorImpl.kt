package com.ku_stacks.ku_ring.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.domain.mapper.toWebViewNotice
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.navigation.NavigationController
import com.ku_stacks.ku_ring.navigation.keys.ArchiveKey
import com.ku_stacks.ku_ring.navigation.keys.AuthFlowKey
import com.ku_stacks.ku_ring.navigation.keys.ClubDetailKey
import com.ku_stacks.ku_ring.navigation.keys.ClubOnboardingKey
import com.ku_stacks.ku_ring.navigation.keys.ClubSubscriptionKey
import com.ku_stacks.ku_ring.navigation.keys.EditDepartmentsKey
import com.ku_stacks.ku_ring.navigation.keys.EditSubscriptionKey
import com.ku_stacks.ku_ring.navigation.keys.FeedbackKey
import com.ku_stacks.ku_ring.navigation.keys.KuringBotKey
import com.ku_stacks.ku_ring.navigation.keys.LibrarySeatKey
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import com.ku_stacks.ku_ring.navigation.keys.NotificationKey
import com.ku_stacks.ku_ring.navigation.keys.OpenSourceKey
import com.ku_stacks.ku_ring.navigation.keys.NotionViewKey
import com.ku_stacks.ku_ring.navigation.keys.OnboardingKey
import com.ku_stacks.ku_ring.navigation.keys.SearchKey
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import javax.inject.Inject

class KuringNavigatorImpl @Inject constructor() : KuringNavigator {

    var navigationController: NavigationController? = null

    override fun navigateToEditSubscription(activity: Activity) {
        navigationController?.navigate(EditSubscriptionKey) ?: return
    }

    override fun navigateToEditSubscribedDepartment(activity: Activity) {
        navigationController?.navigate(EditDepartmentsKey) ?: return
    }

    override fun navigateToFeedback(activity: Activity) {
        navigationController?.navigate(FeedbackKey) ?: return
    }

    override fun createMainIntent(context: Context, route: MainScreenRoute): Intent {
        return Intent().setClassName(context, "com.ku_stacks.ku_ring.HostActivity").apply {
            putExtra(INTENT_KEY_ROUTE, route.route)
        }
    }

    override fun navigateToMain(activity: Activity) {
        navigationController?.navigate(MainHubKey()) ?: return
    }

    override fun navigateToMain(
        activity: Activity,
        url: String,
        articleId: String,
        id: Int,
        category: String,
        subject: String,
    ) {
        navigationController?.navigate(
            NoticeWebKey(
                url = url,
                articleId = articleId,
                id = id.toString(),
                category = category,
                subject = subject,
            )
        ) ?: return
    }

    override fun navigateToArchive(activity: Activity) {
        navigationController?.navigate(ArchiveKey) ?: return
    }

    override fun navigateToSearch(activity: Activity) {
        navigationController?.navigate(SearchKey) ?: return
    }

    override fun navigateToNotification(activity: Activity) {
        navigationController?.navigate(NotificationKey) ?: return
    }

    override fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        id: Int?,
        category: String?,
        subject: String?,
    ): Intent {
        if (url == null || articleId == null || category == null || id == null) {
            throw IllegalArgumentException("intent parameters shouldn't be null: $url, $articleId, $id, $category")
        }
        return Intent().setClassName(context, "com.ku_stacks.ku_ring.HostActivity").apply {
            putExtra(
                WebViewNotice.EXTRA_KEY,
                WebViewNotice(url, articleId, id, category, subject.orEmpty()),
            )
        }
    }

    override fun navigateToNoticeWeb(activity: Activity, notice: Notice) {
        val wv = notice.toWebViewNotice()
        navigationController?.navigate(
            NoticeWebKey(wv.url, wv.articleId, wv.id.toString(), wv.category, wv.subject)
        ) ?: return
    }

    override fun navigateToNoticeWeb(activity: Activity, webViewNotice: WebViewNotice) {
        navigationController?.navigate(
            NoticeWebKey(
                webViewNotice.url,
                webViewNotice.articleId,
                webViewNotice.id.toString(),
                webViewNotice.category,
                webViewNotice.subject,
            )
        ) ?: return
    }

    override fun navigateToNotionView(activity: Activity, notionUrl: String) {
        navigationController?.navigate(NotionViewKey(notionUrl)) ?: return
    }

    override fun navigateToOnboarding(activity: Activity) {
        navigationController?.navigate(OnboardingKey) ?: return
    }

    override fun navigateToSplash(activity: Activity) {
        navigationController?.navigate(SplashKey) ?: return
    }

    override fun navigateToOssLicensesMenu(activity: Activity) {
        val intent = Intent(activity, OssLicensesMenuActivity::class.java)
            .putExtra("title", activity.getString(R.string.open_source_license))
        activity.startActivity(intent)
    }

    override fun navigateToKuringBot(context: Context) {
        navigationController?.navigate(KuringBotKey) ?: return
    }

    override fun navigateToLibrarySeat(activity: Activity) {
        navigationController?.navigate(LibrarySeatKey) ?: return
    }

    override fun navigateToAuth(context: Context) {
        navigationController?.navigate(AuthFlowKey("SIGN_IN")) ?: return
    }

    override fun navigateToSignOut(context: Context) {
        navigationController?.navigate(AuthFlowKey("SIGN_OUT")) ?: return
    }

    override fun navigateToClubOnboarding(context: Context) {
        navigationController?.navigate(ClubOnboardingKey) ?: return
    }

    override fun navigateToClubDetail(context: Context, clubId: Int) {
        navigationController?.navigate(ClubDetailKey(clubId)) ?: return
    }

    override fun navigateToClubSubscription(context: Context) {
        navigationController?.navigate(ClubSubscriptionKey) ?: return
    }

    override fun navigateToOpenSource() {
        navigationController?.navigate(OpenSourceKey) ?: return
    }

    companion object {
        private const val INTENT_KEY_ROUTE = "MAIN_SCREEN_ROUTE"
    }
}
