package com.ku_stacks.ku_ring.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
}