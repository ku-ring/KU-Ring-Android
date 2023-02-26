package com.ku_stacks.ku_ring.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ku_stacks.ku_ring.R
import timber.log.Timber

@BindingAdapter("timeMillis")
fun TextView.getDay(str: String) {
    text = when (str.length) {
        8 -> { // "20211018"
            str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8)
        }
        19 -> { // "2021-10-10 21:37:37"
            str.substring(0, 4) + "." + str.substring(5, 7) + "." + str.substring(8, 10)
        }
        else -> {
            Timber.e("timeMillis String length is not normal in BindingAdapter")
            str
        }
    }
}

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
        isRead -> {
            View.GONE
        }
        isNew -> {
            View.VISIBLE
        }
        else -> {
            View.GONE
        }
    }
    background = when {
        isSaved -> ContextCompat.getDrawable(this.context, R.drawable.point_primary_green)
        isSubscribing -> ContextCompat.getDrawable(this.context, R.drawable.point_primary_pink)
        else -> ContextCompat.getDrawable(this.context, R.drawable.point_primary_gray)
    }
}

@BindingAdapter("textColorGrayIf")
fun TextView.textColorGrayIf(value: Boolean) {
    if (value) {
        setTextColor(this.context.getColor(R.color.kus_tertiary))
    } else {
        setTextColor(this.context.getColor(R.color.kus_primary))
    }
}

@BindingAdapter("confirmedImageIf")
fun ImageView.confirmedImageIf(value: Boolean) {
    if (value) {
        setImageResource(R.drawable.ic_check)
    } else {
        setImageResource(R.drawable.ic_close)
    }
}

@BindingAdapter("enableFeedbackButtonIf")
fun Button.enableFeedbackButtonIf(value: Boolean) {
    background = if (value) {
        ContextCompat.getDrawable(this.context, R.drawable.button_primary_gradation)
    } else {
        ContextCompat.getDrawable(this.context, R.drawable.button_primary_gradation_disabled)
    }
}

@BindingAdapter("disableStartButtonIf")
fun Button.disableStartButtonIf(value: Boolean) {
    if (value) {
        background = ContextCompat.getDrawable(this.context, R.drawable.button_white_disabled)
        setTextColor(ContextCompat.getColor(this.context, R.color.kus_green))
    } else {
        background = ContextCompat.getDrawable(this.context, R.drawable.button_white)
        setTextColor(ContextCompat.getColor(this.context, R.color.kus_green))
    }
}

@BindingAdapter("isNoticeSaved")
fun ImageButton.setBookmarkButtonResource(value: Boolean) {
    val sourceId = if (value) {
        R.drawable.ic_bookmark_filled
    } else {
        R.drawable.ic_bookmark_border
    }
    setImageResource(sourceId)
}