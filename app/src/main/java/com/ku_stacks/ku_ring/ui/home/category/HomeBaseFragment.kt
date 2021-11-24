package com.ku_stacks.ku_ring.ui.home.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.databinding.FragmentHomeCategoryBinding
import com.ku_stacks.ku_ring.ui.detail.DetailActivity
import com.ku_stacks.ku_ring.ui.home.HomeActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class HomeBaseFragment : Fragment(){
    protected val disposable = CompositeDisposable()

    protected lateinit var binding: FragmentHomeCategoryBinding
    protected lateinit var pagingAdapter: NoticePagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_category,container,false)
        binding.lifecycleOwner = activity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagingAdapter = NoticePagingAdapter(
            { notice ->
                (activity as HomeActivity).updateNoticeTobeRead(notice)
                startDetailActivity(notice)
            },
            { notice -> (activity as HomeActivity).insertNotice(notice.articleId, notice.category) }
        )

        binding.categoryRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.categoryRecyclerview.adapter = pagingAdapter

        observePagingState()
    }

    private fun observePagingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showShimmerView()
                    }
                    is LoadState.NotLoading -> {
                        hideShimmerView()
                    }
                    is LoadState.Error -> {
                        //TODO error
                    }
                }
            }
        }
    }

    private fun showShimmerView() {
        binding.homeShimmerLayout.startShimmer()
        binding.categoryRecyclerview.visibility = View.GONE
        binding.homeShimmerLayout.visibility = View.VISIBLE
    }

    private fun hideShimmerView() {
        binding.homeShimmerLayout.stopShimmer()
        binding.categoryRecyclerview.visibility = View.VISIBLE
        binding.homeShimmerLayout.visibility = View.GONE
    }

    private fun startDetailActivity(notice: Notice){
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra("url", notice.url)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }

    override fun onDestroyView() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}