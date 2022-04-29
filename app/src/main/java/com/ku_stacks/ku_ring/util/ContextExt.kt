package com.ku_stacks.ku_ring.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.ku_stacks.ku_ring.ui.dialogs.KuringDialog

fun View.showSnackBar(msg: String) =
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()

fun Context.showToast(msg: String) =
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

fun Context.makeDialog(title: String?, description: String?): KuringDialog {
    val dialog = KuringDialog(this)
        .setText(_title = title, _description = description)
    dialog.show()
    return dialog
}