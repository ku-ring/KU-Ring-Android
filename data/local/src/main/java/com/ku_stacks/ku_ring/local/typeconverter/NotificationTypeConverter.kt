package com.ku_stacks.ku_ring.local.typeconverter

import androidx.room.TypeConverter
import com.ku_stacks.ku_ring.local.entity.PushContent
import kotlinx.serialization.json.Json

class PushTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromContent(content: PushContent): String {
        return json.encodeToString(content)
    }

    @TypeConverter
    fun toContent(contentHtml: String): PushContent {
        return json.decodeFromString(contentHtml)
    }
}
