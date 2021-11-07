package com.ku_stacks.ku_ring.ui.home.category._0_Bachelor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui.home.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BachelorFragment : HomeBaseFragment() {

    private val viewModel by viewModels<BachelorViewModel>()

    @Inject
    lateinit var analytics : EventAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices().subscribe {
            pagingAdapter.submitData(lifecycle, it)
            Timber.e("notice page received")
            hideShimmerView()
        })
    }
}