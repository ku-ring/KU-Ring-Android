package com.ku_stacks.ku_ring.kuringbot.mapper

import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.kuringbot.KuringBotUIMessage

internal fun List<KuringBotMessage>.toUIMessages(): List<KuringBotUIMessage> =
    map { it.toUIMessage() }

internal fun KuringBotMessage.toUIMessage(): KuringBotUIMessage = when (isQuery) {
    true -> KuringBotUIMessage.Question(
        id = id,
        message = message,
        postedTime = postedDate,
    )

    false -> KuringBotUIMessage.Response(
        id = id,
        message = message,
        postedTime = postedDate,
    )
}