package com.ku_stacks.ku_ring.ui.search.fragment_staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.websocket.response.SearchStaffResponse
import com.ku_stacks.ku_ring.databinding.ItemStaffBinding

class SearchStaffAdapter(

) : ListAdapter<SearchStaffResponse, SearchStaffViewHolder>(StaffDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStaffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_staff, parent, false)
        val binding = ItemStaffBinding.bind(view)
        return SearchStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchStaffViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object StaffDiffCallback : DiffUtil.ItemCallback<SearchStaffResponse>() {
        override fun areItemsTheSame(oldItem: SearchStaffResponse, newItem: SearchStaffResponse): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: SearchStaffResponse, newItem: SearchStaffResponse): Boolean {
            return oldItem.email == newItem.email
        }

    }
}