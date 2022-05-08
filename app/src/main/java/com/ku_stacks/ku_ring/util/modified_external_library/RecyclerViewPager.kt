package com.ku_stacks.ku_ring.util.modified_external_library

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import timber.log.Timber

class RecyclerViewPager(
    recyclerView: RecyclerView,
    private val isLoading: () -> Boolean,
    private val loadNext: (Int) -> Unit,
    private val isEnd: () -> Boolean
) : RecyclerView.OnScrollListener() {

    var prefetchDistance: Int = 4
    private var currentPage: Int = 1

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (isLoading()) {
            return
        }
        if (isEnd()) {
            return
        }

        recyclerView.layoutManager?.let {
            val totalItemCount = it.itemCount
            val lastVisibleItemPosition = when (it) {
                is GridLayoutManager -> it.findLastVisibleItemPosition()
                is LinearLayoutManager -> it.findLastVisibleItemPosition()
                is StaggeredGridLayoutManager -> throw Exception("StaggeredGridLayoutManager is not supported")
                else -> return
            }

            if (lastVisibleItemPosition + prefetchDistance >= totalItemCount) {
                val page = ++currentPage
                Timber.e("nextPage: $page")
                loadNext(page)
            }
        }
    }

    fun resetPage() {
        this.currentPage = 1
    }
}