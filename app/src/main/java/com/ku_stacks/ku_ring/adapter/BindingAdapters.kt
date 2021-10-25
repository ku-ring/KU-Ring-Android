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
        text = if (str.length != 8) {
            Timber.e("timeMillis String length is not 8 in BindingAdapter")
            str
        } else {
            str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8)
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