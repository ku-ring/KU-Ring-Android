package com.ku_stacks.ku_ring.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(msg: String) =
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()

fun Context.showToast(msg: String) =
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()