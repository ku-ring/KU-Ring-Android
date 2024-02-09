package com.ku_stacks.ku_ring.main.search.fragment_staff

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.databinding.ItemStaffBinding

@Deprecated("SearchActivity 완성되면 제거")
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
