package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.SavedNoticeEntity
import com.ku_stacks.ku_ring.data.model.SavedNotice

fun SavedNotice.toEntity(): SavedNoticeEntity =
    SavedNoticeEntity(articleId, category, baseUrl, postedDate, subject)