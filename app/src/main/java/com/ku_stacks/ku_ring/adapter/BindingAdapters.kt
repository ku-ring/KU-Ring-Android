package com.ku_stacks.ku_ring.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ku_stacks.ku_ring.R
import timber.log.Timber

object BindingAdapters {
    @JvmStatic
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

    @JvmStatic
    @BindingAdapter("visibleIf")
    fun View.visibleIf(value: Boolean) {
        visibility = when (value) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["isNew", "isSubscribing"])
    fun View.pointColor(isNew: Boolean, isSubscribing: Boolean) {
        visibility = when (isNew) {
            true -> View.VISIBLE
            else -> View.GONE
        }
        background = when (isSubscribing) {
            true -> ContextCompat.getDrawable(this.context, R.drawable.point_primary_pink)
            else -> ContextCompat.getDrawable(this.context, R.drawable.point_primary_gray)
        }
    }

    @JvmStatic
    @BindingAdapter("backgroundGrayIf")
    fun View.backgroundGrayIf(value: Boolean) {
        if(value) {
            setBackgroundColor(Color.parseColor("#14000000"))
        }
        else {
            setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }
}