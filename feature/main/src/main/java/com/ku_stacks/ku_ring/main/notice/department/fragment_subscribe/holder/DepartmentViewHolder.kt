package com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe.holder

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.ItemDepartmentBinding
import com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe.listener.DepartmentEventListener

class DepartmentViewHolder constructor(
    private val binding: ItemDepartmentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(department: Department) {
        binding.uiModel = department

        binding.subscribeDepartmentButton.apply {
            if (department.isSubscribed) {
                this.setImageResource(R.drawable.ic_check_green)
                this.colorFilter = null
            } else {
                this.setImageResource(R.drawable.ic_plus)
                this.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.kus_primary
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup, eventListener: DepartmentEventListener): DepartmentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDepartmentBinding.inflate(layoutInflater, parent, false).apply {
                this.eventListener = eventListener
            }
            return DepartmentViewHolder(binding)
        }
    }
}
