package com.ku_stacks.ku_ring.util.modified_external_library

import androidx.annotation.MainThread
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import timber.log.Timber

class RecyclerViewPager(
    recyclerView: RecyclerView,
    private val isReversed: Boolean,
    private val isLoading: () -> Boolean,
    private val loadNext: (Int) -> Unit,
    private val isEnd: () -> Boolean
) : RecyclerView.OnScrollListener() {

    var prefetchDistance: Int = 4
    private var currentPage: Int = 1

    init {
        recyclerView.addOnScrollListener(this)
    }

    @MainThread
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (isLoading()) {
            return
        }
        if (isEnd()) {
            return
        }

        recyclerView.layoutManager?.let {
            if (isReversed) {
                val visibleItemCount = it.childCount

                val firstVisibleItemPosition = when (it) {
                    is GridLayoutManager -> it.findLastVisibleItemPosition()
                    is LinearLayoutManager -> it.findLastVisibleItemPosition()
                    is StaggeredGridLayoutManager -> throw Exception("StaggeredGridLayoutManager is not supported")
                    else -> return
                }

                if (firstVisibleItemPosition - visibleItemCount <= prefetchDistance) {
                    val page = ++currentPage
                    Timber.e(" nextPage: $page")
                    loadNext(page)
                }
            } else {
                val totalItemCount = it.itemCount

                val lastVisibleItemPosition = when (it) {
                    is GridLayoutManager -> it.findLastVisibleItemPosition()
                    is LinearLayoutManager -> it.findLastVisibleItemPosition()
                    is StaggeredGridLayoutManager -> throw Exception("StaggeredGridLayoutManager is not supported")
                    else -> return
                }

                if (lastVisibleItemPosition + prefetchDistance >= totalItemCount) {
                    val page = ++currentPage
                    Timber.e(" nextPage: $page")
                    loadNext(page)
                }
            }
        }
    }

    fun resetPage() {
        this.currentPage = 1
    }
}