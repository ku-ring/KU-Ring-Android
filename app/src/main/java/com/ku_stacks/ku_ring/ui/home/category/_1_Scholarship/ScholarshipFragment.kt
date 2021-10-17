package com.ku_stacks.ku_ring.ui.home.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.ui.home.category._0_Bachelor.BachelorViewModel
import com.ku_stacks.ku_ring.ui.home.category._1_Scholarship.ScholarshipViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScholarshipFragment: HomeBaseFragment() {

    private val viewModel by viewModels<ScholarshipViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices().subscribe {
            pagingAdapter.submitData(lifecycle, it)
            Timber.e("notice page received")
        })
    }

}