package com.ku_stacks.ku_ring.club.detail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ku_stacks.ku_ring.club.R

sealed class SnsUrl(
    open val url: String,
    @DrawableRes open val iconResId: Int,
    @StringRes open val textResId: Int,
) {
    data class Instagram(override val url: String) : SnsUrl(
        url = url,
        iconResId = R.drawable.ic_instagram_v2,
        textResId = R.string.club_detail_instagram_url,
    )

    data class YouTube(override val url: String) : SnsUrl(
        url = url,
        iconResId = R.drawable.ic_youtube_v2,
        textResId = R.string.club_detail_youtube_url,
    )

    data class Other(override val url: String) : SnsUrl(
        url = url,
        iconResId = R.drawable.ic_link_v2,
        textResId = R.string.club_detail_other_url,
    )

    companion object {
        fun from(url: String): SnsUrl {
            return when {
                url.contains("instagram.com") -> Instagram(url)
                url.contains("youtube.com") -> YouTube(url)
                else -> Other(url)
            }
        }
    }
}
