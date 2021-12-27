package com.ku_stacks.ku_ring.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.R
import timber.log.Timber


open class SwipeToDeleteCallback(context: Context, private val buttonAction: ButtonAction) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteSideMarginDp = 18f
    private val deleteSideMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, deleteSideMarginDp, context.resources.displayMetrics).toInt()

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_trash)!!
    private val intrinsicWidth = deleteIcon.intrinsicWidth
    private val intrinsicHeight = deleteIcon.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#e45b78")

    private var currentDx = 0f
    private var rightWidth = 0

    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    //private var curPosition : Int? = null

    private var previousViewHolder : RecyclerView.ViewHolder? = null
    //private var prevPosition : Int? = null

    interface ButtonAction {
        fun onClickDelete(position : Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

    private fun drawClampedBackground(canvas: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(0 , itemView.top, itemView.right, itemView.bottom)
        background.draw(canvas)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteSideMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteSideMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(canvas)
    }

    //recyclerview의 view가 반응할때마다 호출된다.
    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        currentDx = dX
        rightWidth = intrinsicWidth + 2 * deleteSideMargin

        val isClamped = getViewHolderTag(viewHolder)
        val x = clampViewPositionHorizontal(dX, isClamped)

        viewHolder.itemView.translationX = x

        drawClampedBackground(canvas, viewHolder)
        currentItemViewHolder = viewHolder
        //curPosition = viewHolder.absoluteAdapterPosition
    }


    //swipe 해서 손을 떼었을 때 호출된다. 이때 setViewHolderTag() 를 설정하면 된다!!
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        Timber.e("getSwipeThreshold called")

        if(currentDx <= -rightWidth) {
            setViewHolderTag(viewHolder, true)
        } else { //정확히 currentDx가 rightWidth만큼 당겨져야하는지, 그 중간이 될지는 추가 논의필요
            setViewHolderTag(viewHolder, false)
        }

        return 2f
    }

    //사용자 interaction이 끝났을때 호출됨(스와이프 포함)
    //하지만 clearView()는 onChildDraw()가 완전히 끝났을때 호출되기 때문에
    //여기서 setViewHolder() 세팅을하면 ui 적용이 안된다.
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        Timber.e("clearView called")
        addDeleteButtonClickListener(recyclerView, viewHolder)
    }

    //스와이프(또는 드레그) 시작, 종료시 각각 1번씩 호출됨
    //주의 : viewHolder는 interaction의 종료시에는 null로 반환됨
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        //swipe interaction 시작 시에만 이벤트 처리
        viewHolder?.let {
            previousViewHolder = it
        }

        Timber.e("onSelectedChanged called , viewHolder : $viewHolder")
        super.onSelectedChanged(viewHolder, actionState)
    }


    //clamp 되어 화면에 걸쳐있으면 tag를 true로 둔다
    private fun setViewHolderTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getViewHolderTag(viewHolder: RecyclerView.ViewHolder?) : Boolean {
        return viewHolder?.itemView?.tag as? Boolean ?: false
    }

    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean
    ) : Float {
        val min: Float = -rightWidth.toFloat()
        val max = 0f

        val x = if (isClamped) {
            dX - rightWidth
        } else {
            dX
        }
        return Math.min(Math.max(min, x), max)
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 0f
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setScrollListener(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                if (e.action == MotionEvent.ACTION_DOWN) {

                    rv.findChildViewUnder(e.x, e.y)?.let {
                        val viewHolder = rv.getChildViewHolder(it)
                        if(viewHolder.absoluteAdapterPosition != previousViewHolder?.absoluteAdapterPosition) {
                            restorePreviousViewHolder()
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) { }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }

        })
        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            restorePreviousViewHolder()
            Timber.e("scroll changed")
        }
    }

    private fun restorePreviousViewHolder() {
        //다른 아이탬 clamp 제거
        previousViewHolder?.apply {
            setViewHolderTag(this, false)
            itemView.animate().translationX(0f).duration = 300L
            previousViewHolder = null
        }
    }

    fun onDraw(canvas: Canvas) {
        currentItemViewHolder?.let {
            if (getViewHolderTag(it)) {
                drawClampedBackground(canvas, it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addDeleteButtonClickListener(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (deleteIcon.bounds.contains(event.x.toInt(), event.y.toInt())) {
                    recyclerView.setOnTouchListener { _, event2 ->
                        if (event2.action == MotionEvent.ACTION_UP) {
                            if (deleteIcon.bounds.contains(event2.x.toInt(), event2.y.toInt())) {
                                buttonAction.onClickDelete(viewHolder.absoluteAdapterPosition)
                            }
                        }
                        false
                    }
                }
            }
            return@setOnTouchListener false
        }
    }
}