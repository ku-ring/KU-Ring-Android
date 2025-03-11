package com.ku_stacks.ku_ring.auth.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.auth.compose.signin.SignInDestination
import com.ku_stacks.ku_ring.auth.compose.signin.signInNavGraph

@Composable
internal fun AuthScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SignInDestination.SignIn,
        modifier = modifier.fillMaxSize()
    ) {
        signInNavGraph(
            onNavigateUp = onNavigateUp,
            onNavigateToMain = { }, //TODO: 로그인으로 진입하기 전 화면으로 이동
            onNavigateToSignUp = { }, // TODO: 회원가입 화면으로 이동
            onNavigateToFindPassword = {},  //TODO: 비밀번호 찾기 화면으로 이동
        )
    }
}