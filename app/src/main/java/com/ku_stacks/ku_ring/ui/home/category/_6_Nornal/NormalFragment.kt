package com.ku_stacks.ku_ring.ui.home.category._6_Nornal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.ui.home.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NormalFragment : HomeBaseFragment(){
    private val viewModel by viewModels<NormalViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices().subscribe {
            pagingAdapter.submitData(lifecycle, it)
            Timber.e("notice page received")
            hideShimmerView()
        })
    }
}