package com.ku_stacks.ku_ring.ui.home.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.ui.home.category._2_employ.EmployViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployFragment : HomeBaseFragment(){

    private val viewModel by viewModels<EmployViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices(lifecycleScope).subscribe {
            pagingAdapter.submitData(lifecycle, it)
        })
    }
}