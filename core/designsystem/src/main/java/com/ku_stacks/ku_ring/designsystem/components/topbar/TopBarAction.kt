package com.ku_stacks.ku_ring.designsystem.components.topbar

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable

/**
 * CenterTitleTopBar에서 사용되는 탑바 액션의 속성을 정의하는 객체이다.
 *
 * @param action 텍스트 오른쪽에 표시될 작업. [Icon] 등을 넣을 수 있다.
 * @param onClick 액션 텍스트를 클릭할 때 실행할 콜백.
 * @param clickLabel 액션 클릭 콜백을 설명하는 텍스트. 콜백이 null이 아니라면 접근성을 위해 제공하는 것이 좋다.
 */
data class TopBarAction (
    val action: @Composable () -> Unit,
    val onClick: (() -> Unit)? = null,
    val clickLabel: String? = null,
)
