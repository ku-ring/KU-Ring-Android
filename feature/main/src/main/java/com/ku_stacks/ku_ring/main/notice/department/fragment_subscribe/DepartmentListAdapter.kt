package com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe.holder.DepartmentViewHolder
import com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe.listener.DepartmentEventListener

class DepartmentListAdapter(
    private val departmentEventListener: DepartmentEventListener
) : ListAdapter<Department, RecyclerView.ViewHolder>(DepartmentItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DepartmentViewHolder.from(parent, departmentEventListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = currentList.getOrNull(position)) {
            is Department -> (holder as? DepartmentViewHolder)?.bind(item)
            else -> Unit
        }
    }

    private class DepartmentItemDiffCallback : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem == newItem
        }
    }
}
