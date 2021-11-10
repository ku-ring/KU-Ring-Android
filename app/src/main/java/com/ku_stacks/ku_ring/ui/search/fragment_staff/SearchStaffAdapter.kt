package com.ku_stacks.ku_ring.ui.search.fragment_staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.websocket.response.StaffResponse
import com.ku_stacks.ku_ring.databinding.ItemStaffBinding

class SearchStaffAdapter(

) : ListAdapter<StaffResponse, SearchStaffViewHolder>(StaffDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStaffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_staff, parent, false)
        val binding = ItemStaffBinding.bind(view)
        return SearchStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holderSearch: SearchStaffViewHolder, position: Int) {
        getItem(position)?.let {
            holderSearch.bind(it)
        }
    }

    object StaffDiffCallback : DiffUtil.ItemCallback<StaffResponse>() {
        override fun areItemsTheSame(oldItem: StaffResponse, newItem: StaffResponse): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: StaffResponse, newItem: StaffResponse): Boolean {
            return oldItem.email == newItem.email
        }

    }
}