package com.ku_stacks.ku_ring.auth.compose.signin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ku_stacks.ku_ring.auth.compose.AuthDestination
import com.ku_stacks.ku_ring.auth.compose.signin.inner_screen.SignInScreen

internal fun NavGraphBuilder.signInNavGraph(
    navController: NavHostController,
    onNavigateUp: () -> Unit,
) {
    composable<AuthDestination.SignIn> {
        SignInScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToMain = onNavigateUp,
            onNavigateToSignUp = { navController.navigate(AuthDestination.SignUp) },
            onNavigateToFindPassword = { navController.navigate(AuthDestination.ResetPassword) },
        )
    }
}
