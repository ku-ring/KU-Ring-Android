package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDataUiModel
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDateHeaderUiModel

fun List<Push>.toPushUiModelList(): List<PushDataUiModel> {
    val pushDataList = ArrayList<PushDataUiModel>()
    forEachIndexed { index, push ->
        /** 두 알림 날짜를 비교해서 서로 다른 날짜면 PushDateHeaderUiModel 삽입 */
        val isNewDay = if (index == 0) {
            true
        } else {
            val prevItem = this[index - 1]
            prevItem.postedDate != push.postedDate
        }

        if (isNewDay) {
            pushDataList.add(PushDateHeaderUiModel(push.postedDate))
        }
        pushDataList.add(push.toPushContentUiModel())
    }
    return pushDataList
}

fun Push.toPushContentUiModel(): PushContentUiModel {
    return PushContentUiModel(
        articleId = articleId,
        category = category,
        postedDate = postedDate,
        subject = subject,
        baseUrl = baseUrl,
        isNew = isNew,
        receivedDate = receivedDate,
        tag = tag
    )
}

