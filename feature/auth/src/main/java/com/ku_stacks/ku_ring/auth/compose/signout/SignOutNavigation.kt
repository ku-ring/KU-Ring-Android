package com.ku_stacks.ku_ring.auth.compose.signout

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ku_stacks.ku_ring.auth.compose.signout.inner_screen.SignOutCompleteScreen
import com.ku_stacks.ku_ring.auth.compose.signout.inner_screen.SignOutScreen

internal fun NavGraphBuilder.signOutNavGraph(
    onNavigateUp: () -> Unit,
    navController: NavHostController
) {
    navigation<SignOutDestination.SignOut>(
        startDestination = SignOutDestination.SignOutGuide,
    ) {
        composable<SignOutDestination.SignOutGuide> {
            SignOutScreen(
                onNavigateUp = onNavigateUp,
                onNavigateToSignOutComplete = {
                    navController.navigate(SignOutDestination.SignOutComplete)
                },
            )
        }
        composable<SignOutDestination.SignOutComplete> {
            SignOutCompleteScreen(
                onNavigateToMyPage = onNavigateUp,
            )
        }
    }

}