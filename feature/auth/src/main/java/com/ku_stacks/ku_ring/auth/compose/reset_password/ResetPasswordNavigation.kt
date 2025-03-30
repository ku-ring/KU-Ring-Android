package com.ku_stacks.ku_ring.auth.compose.reset_password

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ku_stacks.ku_ring.auth.compose.AuthDestination
import com.ku_stacks.ku_ring.auth.compose.reset_password.inner_screen.EmailVerificationScreen
import com.ku_stacks.ku_ring.auth.compose.reset_password.inner_screen.ReSetPasswordScreen

internal fun NavGraphBuilder.resetPasswordNavGraph(
    navController: NavHostController,
) {
    navigation<AuthDestination.ResetPassword>(
        startDestination = AuthDestination.ResetPasswordEmailVerification,
    ) {
        composable<AuthDestination.ResetPasswordEmailVerification> {
            EmailVerificationScreen(
                onNavigateUp = navController::navigateUp,
                onNavigateToPassword = {
                    navController.navigate(AuthDestination.ResetPasswordSetPassword)
                },
            )
        }
        composable<AuthDestination.ResetPasswordSetPassword> {
            ReSetPasswordScreen(
                onNavigateUp = navController::navigateUp,
                onNavigateToSignIn = {
                    navController.popBackStack(AuthDestination.SignIn, inclusive = false)
                },
            )
        }
    }
}


