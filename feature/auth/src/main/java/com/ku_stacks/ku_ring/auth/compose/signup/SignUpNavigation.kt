package com.ku_stacks.ku_ring.auth.compose.signup

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ku_stacks.ku_ring.auth.compose.AuthDestination
import com.ku_stacks.ku_ring.auth.compose.signup.inner_screen.EmailVerificationScreen
import com.ku_stacks.ku_ring.auth.compose.signup.inner_screen.SetPasswordScreen
import com.ku_stacks.ku_ring.auth.compose.signup.inner_screen.SignUpCompleteScreen
import com.ku_stacks.ku_ring.auth.compose.signup.inner_screen.TermsAndConditionsScreen

internal fun NavGraphBuilder.signUpNavGraph(
    navController: NavHostController,
) {
    navigation<AuthDestination.SignUp>(
        startDestination = AuthDestination.SignUpTermsAndConditions,
    ) {
        composable<AuthDestination.SignUpTermsAndConditions> {
            TermsAndConditionsScreen(
                onNavigateUp = navController::navigateUp,
                onNavigateToEmailVerification = {
                    navController.navigate(AuthDestination.SignUpEmailVerification)
                },
            )
        }
        composable<AuthDestination.SignUpEmailVerification> {
            EmailVerificationScreen(
                onNavigateUp = navController::navigateUp,
                onNavigateToPassword = {
                    navController.navigate(AuthDestination.SignUpSetPassword)
                },
            )
        }
        composable<AuthDestination.SignUpSetPassword> {
            SetPasswordScreen(
                onNavigateUp = navController::navigateUp,
                onNavigateToSignUpComplete = {
                    navController.navigate(AuthDestination.SignUpComplete)
                },
            )
        }
        composable<AuthDestination.SignUpComplete> {
            SignUpCompleteScreen(
                onNavigateToSignIn = {
                    navController.popBackStack(AuthDestination.SignIn, inclusive = false)
                },
            )
        }
    }
}


