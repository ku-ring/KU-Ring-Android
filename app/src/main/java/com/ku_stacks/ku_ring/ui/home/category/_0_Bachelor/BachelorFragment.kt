package com.ku_stacks.ku_ring.ui.home.category._0_Bachelor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui.home.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BachelorFragment : HomeBaseFragment() {

    private val viewModel by viewModels<BachelorViewModel>()

    @Inject
    lateinit var analytics : EventAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices(lifecycleScope).subscribe {
            pagingAdapter.submitData(lifecycle, it)
        })
    }
}