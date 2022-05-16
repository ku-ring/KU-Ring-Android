package com.ku_stacks.ku_ring.ui.main.search.fragment_staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Staff
import com.ku_stacks.ku_ring.databinding.ItemStaffBinding

class SearchStaffAdapter(
    private val itemClick: (Staff) -> Unit,
) : ListAdapter<Staff, SearchStaffViewHolder>(StaffDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStaffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_staff, parent, false)
        val binding = ItemStaffBinding.bind(view)
        return SearchStaffViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: SearchStaffViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object StaffDiffCallback : DiffUtil.ItemCallback<Staff>() {
        override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
            return oldItem.email == newItem.email
        }

    }
}