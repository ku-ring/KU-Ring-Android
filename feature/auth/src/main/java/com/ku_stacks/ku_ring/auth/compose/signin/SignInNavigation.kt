package com.ku_stacks.ku_ring.auth.compose.signin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ku_stacks.ku_ring.auth.compose.signin.inner_screen.SignInScreen

internal fun NavGraphBuilder.signInNavGraph(
    onNavigateUp: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
) {
    composable<SignInDestination.SignIn> {
        SignInScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToMain = onNavigateToMain,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToFindPassword = onNavigateToFindPassword,
        )
    }
}