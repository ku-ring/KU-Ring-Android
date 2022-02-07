package com.ku_stacks.ku_ring.ui.home.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.ui.home.category.stage2_employ.EmployViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployFragment : HomeBaseFragment(){

    private val viewModel by viewModels<EmployViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(
            viewModel.getNotices().subscribe {
                pagingAdapter.submitData(lifecycle, it)
            })
    }
}