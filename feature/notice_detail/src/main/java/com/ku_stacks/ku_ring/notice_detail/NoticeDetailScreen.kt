package com.ku_stacks.ku_ring.notice_detail

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.notice_detail.noticeweb.NoticeWebScreen
import com.ku_stacks.ku_ring.notice_detail.report.ReportCommentScreen

@Composable
internal fun NoticeDetailScreen(
    webViewNotice: WebViewNotice,
    navController: NavHostController,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NoticeDetailRoute.NoticeWeb.from(webViewNotice),
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
    ) {
        composable<NoticeDetailRoute.NoticeWeb> {
            NoticeWebScreen(
                webViewNotice = webViewNotice,
                onNavigateBack = onClose,
                onReportComment = { commentId ->
                    navController.navigate(NoticeDetailRoute.ReportComment(commentId))
                },
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxSize(),
            )
        }
        composable<NoticeDetailRoute.ReportComment> {
            val commentId = it.toRoute<NoticeDetailRoute.ReportComment>().commentId
            ReportCommentScreen(
                reportCommentId = commentId,
                onClose = navController::popBackStack,
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxSize()
            )
        }
    }
}