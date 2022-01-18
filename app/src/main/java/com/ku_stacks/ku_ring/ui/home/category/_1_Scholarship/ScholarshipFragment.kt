package com.ku_stacks.ku_ring.ui.home.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.ui.home.category._1_Scholarship.ScholarshipViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScholarshipFragment: HomeBaseFragment() {

    private val viewModel by viewModels<ScholarshipViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices(lifecycleScope).subscribe {
            pagingAdapter.submitData(lifecycle, it)
        })
    }
}