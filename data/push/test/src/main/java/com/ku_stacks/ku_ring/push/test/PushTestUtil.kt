package com.ku_stacks.ku_ring.push.test

import com.ku_stacks.ku_ring.local.entity.PushEntity

object PushTestUtil {
    fun fakePushEntity() = PushEntity(
        articleId = "ababab",
        category = "bachelor",
        postedDate = "2022-01-14 00:50:33",
        subject = "실감미디어 혁신 공유대학 융합전공 안내",
        fullUrl = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=ababab",
        isNew = true,
        receivedDate = "20220114-005036",
    )
}
