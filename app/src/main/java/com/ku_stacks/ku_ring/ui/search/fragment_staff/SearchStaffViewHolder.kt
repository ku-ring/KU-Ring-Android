package com.ku_stacks.ku_ring.ui.search.fragment_staff

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.model.Staff
import com.ku_stacks.ku_ring.databinding.ItemStaffBinding

class SearchStaffViewHolder(
    private val binding: ItemStaffBinding,
    private val itemClick: (Staff) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(staff: Staff) {
        binding.staffNameTxt.text = staff.name
        binding.staffDepartmentAndCollegeTxt.text = "${staff.department} · ${staff.college}"
        binding.root.setOnClickListener {
            itemClick(staff)
        }
        binding.executePendingBindings()
    }
}