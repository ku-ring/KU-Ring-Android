package com.ku_stacks.ku_ring.ui.home.category._3_nation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.ui.home.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NationFragment : HomeBaseFragment(){
    private val viewModel by viewModels<NationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable.add(viewModel.getNotices().subscribe {
            pagingAdapter.submitData(lifecycle, it)
        })
    }
}