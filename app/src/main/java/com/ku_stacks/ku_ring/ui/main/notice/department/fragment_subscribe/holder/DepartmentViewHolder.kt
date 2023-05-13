package com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.model.Department
import com.ku_stacks.ku_ring.databinding.ItemDepartmentBinding
import com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe.listener.DepartmentEventListener

class DepartmentViewHolder constructor(
    private val binding: ItemDepartmentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(department: Department) {
        binding.uiModel = department
    }

    companion object {
        fun from(parent: ViewGroup, eventListener: DepartmentEventListener): DepartmentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDepartmentBinding.inflate(layoutInflater, parent, false)
            return DepartmentViewHolder(binding)
        }
    }
}
