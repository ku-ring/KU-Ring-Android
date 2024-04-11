package com.ku_stacks.ku_ring.ui_util

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleIf")
fun View.visibleIf(value: Boolean) {
    visibility = when (value) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter(value = ["isNew", "isRead", "isSubscribing", "isSaved"])
fun View.pointColor(isNew: Boolean, isRead: Boolean, isSubscribing: Boolean, isSaved: Boolean) {
    visibility = when {
        isSaved -> View.VISIBLE
        isRead -> View.GONE
        isNew -> View.VISIBLE
        else -> View.GONE
    }
    val backgroundDrawable = when {
        isSaved -> R.drawable.point_primary_green
        isSubscribing -> R.drawable.point_primary_pink
        else -> R.drawable.point_primary_gray
    }
    background = ContextCompat.getDrawable(context, backgroundDrawable)
}

@BindingAdapter("textColorGrayIf")
fun TextView.textColorGrayIf(value: Boolean) {
    val textColor = if (value) {
        R.color.kus_tertiary
    } else {
        R.color.kus_primary
    }
    setTextColor(context.getColor(textColor))
}

@BindingAdapter("confirmedImageIf")
fun ImageView.confirmedImageIf(value: Boolean) {
    val drawable = if (value) {
        R.drawable.ic_check
    } else {
        R.drawable.ic_close
    }
    setImageResource(drawable)
}

@BindingAdapter("enableFeedbackButtonIf")
fun Button.enableFeedbackButtonIf(value: Boolean) {
    val backgroundDrawable = if (value) {
        R.drawable.button_primary_gradation
    } else {
        R.drawable.button_primary_gradation_disabled
    }
    background = ContextCompat.getDrawable(context, backgroundDrawable)
}

@BindingAdapter("disableStartButtonIf")
fun Button.disableStartButtonIf(value: Boolean) {
    val backgroundDrawable = if (value) {
        R.drawable.button_white_disabled
    } else {
        R.drawable.button_white
    }
    background = ContextCompat.getDrawable(context, backgroundDrawable)

    val textColor = R.color.kus_green
    setTextColor(ContextCompat.getColor(context, textColor))
}

@BindingAdapter("isNoticeSaved")
fun ImageButton.setBookmarkButtonResource(value: Boolean) {
    val sourceId = if (value) {
        R.drawable.ic_bookmark_fill_v2
    } else {
        R.drawable.ic_bookmark_v2
    }
    setImageResource(sourceId)
}
