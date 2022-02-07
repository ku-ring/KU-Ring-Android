package com.ku_stacks.ku_ring.ui.home.category.stage5_Industry

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.ui.home.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndustryFragment : HomeBaseFragment(){

    private val viewModel by viewModels<IndustryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(
            viewModel.getNotices().subscribe {
                pagingAdapter.submitData(lifecycle, it)
            })
    }
}