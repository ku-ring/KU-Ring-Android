package com.ku_stacks.ku_ring.main.setting.compose.inner_screen

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.main.setting.compose.SettingDestinations

@Composable
internal fun OpenSourceScreen(
    closeOpenSourceScreen: () -> Unit,
    navigateToOssLicenses: () -> Unit,
    navigateToLottieFiles: () -> Unit,
    navigateToTossFaceCopyright: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SettingDestinations.OPEN_SOURCE_LIST,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } },
    ) {
        composable(SettingDestinations.OPEN_SOURCE_LIST) {
            OpenSourceListScreen(
                onNavigateToBack = closeOpenSourceScreen,
                onNavigateToLottieFiles = navigateToLottieFiles,
                onNavigateToEmoji = { navController.navigate(SettingDestinations.OPEN_SOURCE_EMOJI) },
                onNavigateToOssLicenses = navigateToOssLicenses,
                modifier = modifier,
            )
        }
        composable(SettingDestinations.OPEN_SOURCE_EMOJI) {
            OpenSourceEmojiScreen(
                navigateToBack = { navController.popBackStack() },
                navigateToCopyright = navigateToTossFaceCopyright,
                modifier = modifier,
            )
        }
    }
}