package com.ku_stacks.ku_ring.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()