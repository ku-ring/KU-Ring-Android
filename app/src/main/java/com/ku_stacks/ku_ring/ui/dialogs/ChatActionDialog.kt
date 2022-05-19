package com.ku_stacks.ku_ring.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.ku_stacks.ku_ring.databinding.DialogChatActionBinding

class ChatActionDialog(context: Context) : Dialog(context) {

    private lateinit var binding: DialogChatActionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogChatActionBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
    }

    fun setOnCopyMessageClickListener(onClickListener: View.OnClickListener) = this.apply {
        binding.actionCopyContentTv.setOnClickListener { v ->
            onClickListener.onClick(v)
            dismiss()
        }
    }

    fun setOnCopyNicknameClickListener(onClickListener: View.OnClickListener) = this.apply {
        binding.actionCopyNicknameTv.setOnClickListener { v ->
            onClickListener.onClick(v)
            dismiss()
        }
    }

    fun setOnReportClickListener(onClickListener: View.OnClickListener) = this.apply {
        binding.actionReportTv.setOnClickListener { v ->
            onClickListener.onClick(v)
            dismiss()
        }
    }

    fun setOnBlockClickListener(onClickListener: View.OnClickListener) = this.apply {
        binding.actionBlockTv.setOnClickListener { v ->
            onClickListener.onClick(v)
            dismiss()
        }
    }
}