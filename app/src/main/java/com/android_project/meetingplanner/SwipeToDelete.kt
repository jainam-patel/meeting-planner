package com.android_project.meetingplanner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SwipeToDelete(
    private val listener: OnSwipeListener,
    val context: Context,
    val recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete_icon)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom.toFloat() - itemView.top.toFloat() + 75
        val iconMargin = (itemHeight - (deleteIcon?.intrinsicHeight ?: 0)) / 2

        val iconTop = itemView.top.toFloat() + (itemHeight - deleteIcon?.intrinsicHeight!!) / 2
        val iconBottom = iconTop + deleteIcon.intrinsicHeight
        val deleteIconLeft = itemView.right.toFloat() - 20 - deleteIcon.intrinsicWidth
        val deleteIconRight = itemView.right.toFloat() - 20

        val deleteIconClickableRegion = Rect(
            deleteIconLeft.toInt(),
            iconTop.toInt(),
            deleteIconRight.toInt(),
            iconBottom.toInt()
        )

        recyclerView.setOnTouchListener { view, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (deleteIconClickableRegion.contains(x, y)) {
                    listener.onDeleteClick(viewHolder.adapterPosition)
                }
            }
            return@setOnTouchListener false
        }
        listener.onSwipe(position)

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom.toFloat() - itemView.top.toFloat() + 75
        val itemWidth = itemView.right.toFloat() - itemView.left.toFloat()  + 50
        val maxSwipeDistance = itemWidth * 0.15f

        val iconMargin = (itemHeight - (deleteIcon?.intrinsicHeight ?: 0)) / 2
        if (dX < 0) {
            val iconTop = itemView.top.toFloat() + (itemHeight - deleteIcon?.intrinsicHeight!!) / 2
            val iconBottom = iconTop + deleteIcon.intrinsicHeight
            val deleteIconLeft = itemView.right.toFloat() - deleteIcon.intrinsicWidth - 20
            val deleteIconRight = itemView.right.toFloat() - 20

            deleteIcon.setBounds(
                deleteIconLeft.toInt(),
                iconTop.toInt(),
                deleteIconRight.toInt(),
                iconBottom.toInt()
            )
            deleteIcon.draw(c)

            if (dX <= -maxSwipeDistance) {
                viewHolder.itemView.translationX = -maxSwipeDistance
            } else {
                viewHolder.itemView.translationX = dX
            }
        }
    }

    interface OnSwipeListener {
        fun onSwipe(position: Int)
        fun onDeleteClick(position: Int)
    }

}