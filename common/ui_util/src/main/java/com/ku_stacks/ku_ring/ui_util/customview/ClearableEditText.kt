package com.ku_stacks.ku_ring.ui_util.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.addTextChangedListener
import com.ku_stacks.ku_ring.ui_util.R

class ClearableEditText : AppCompatEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr)

    private lateinit var clearDrawable: Drawable

    init {
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        val rawDrawable = ContextCompat.getDrawable(context, R.drawable.ic_close)!!
        clearDrawable = DrawableCompat.wrap(rawDrawable)

        DrawableCompat.setTintList(clearDrawable, hintTextColors)
        clearDrawable.setBounds(0, 0, clearDrawable.intrinsicWidth, clearDrawable.intrinsicHeight)

        addTextChangedListener { editable ->
            val isVisible = editable?.isNotEmpty() ?: false
            showOrHideClearDrawable(isVisible)
        }

        setOnTouchListener { _, motionEvent ->
            motionEvent?.x?.let { x ->
                if (clearDrawable.isVisible && x > width - paddingRight - clearDrawable.intrinsicWidth) {
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        error = null
                        text = null
                    }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun showOrHideClearDrawable(visible: Boolean) {
        setCompoundDrawables(compoundDrawables[0], null, if (visible) clearDrawable else null, null)
    }

}