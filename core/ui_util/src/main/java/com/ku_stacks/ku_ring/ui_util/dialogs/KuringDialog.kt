package com.ku_stacks.ku_ring.ui_util.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.ku_stacks.ku_ring.ui_util.R

class KuringDialog(context: Context) : Dialog(context) {

    private var title: String? = null
    private var description: String? = null
    private var leftButtonText: String? = null
    private var rightButtonText: String? = null

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var cancelButton: TextView
    private lateinit var confirmButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.dialog_kuring)

        titleTextView = findViewById(R.id.title_txt)
        descriptionTextView = findViewById(R.id.description_txt)
        cancelButton = findViewById(R.id.dialog_cancel_bt)
        confirmButton = findViewById(R.id.dialog_confirm_bt)

        cancelButton.setOnClickListener {
            dismiss()
        }
        confirmButton.setOnClickListener {
            dismiss()
        }

        setCanceledOnTouchOutside(false)
    }

    fun setText(_title: String?, _description: String?) = this.apply {
        title = _title
        description = _description
    }

    fun setButtonText(leftText: String?, rightText: String?) = this.apply {
        leftButtonText = leftText
        rightButtonText = rightText
    }

    fun setOnConfirmClickListener(onClickListener: View.OnClickListener) = this.apply {
        confirmButton.setOnClickListener { v ->
            onClickListener.onClick(v)
            dismiss()
        }
    }

    fun setOnCancelClickListener(onClickListener: View.OnClickListener) = this.apply {
        cancelButton.setOnClickListener { v ->
            onClickListener.onClick(v)
            dismiss()
        }
    }

    override fun show() {
        super.show()
        title?.let {
            titleTextView.text = it
        }
        description?.let {
            descriptionTextView.text = it
        }

        leftButtonText?.let {
            cancelButton.text = it
        }
        rightButtonText?.let {
            confirmButton.text = it
        }

        titleTextView.isVisible = title != null
        descriptionTextView.isVisible = description != null
    }
}