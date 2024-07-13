package com.ku_stacks.ku_ring.designsystem.utils

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * 클릭 시 ripple 효과 제거하는 InteractionSource
 * 참고한 링크 : https://semicolonspace.com/jetpack-compose-disable-ripple-effect/
 */

class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}
