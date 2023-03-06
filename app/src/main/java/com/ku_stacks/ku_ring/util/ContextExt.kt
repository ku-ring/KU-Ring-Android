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

fun Context.makeDialog(title: String? = null, description: String? = null): KuringDialog {
    return KuringDialog(this).apply {
        setText(_title = title, _description = description)
        show()
    }
}

fun Context.makeDialog(
    title: String? = null,
    description: String? = null,
    leftText: String?,
    rightText: String
): KuringDialog {
    return KuringDialog(this).apply {
        setText(_title = title, _description = description)
        setButtonText(leftText = leftText, rightText = rightText)
        show()
    }
}
