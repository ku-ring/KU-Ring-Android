package com.ku_stacks.ku_ring.domain.noticecomment.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import timber.log.Timber

/**
 * Paging source which loads comments. This class should be instantiated per notice.
 *
 * Comment paging supports only forward loading. So the prevKey is always null, and the nextKey is
 * next integer value of the last comment id, if there are more comments, otherwise null.
 *
 * Note that comments are paged by the comment id, not the page number.
 */
internal class GetNoticeCommentPagingSource(
    private val noticeCommentRepository: NoticeCommentRepository,
    private val noticeId: Int,
) : PagingSource<Int, NoticeComment>() {

    private var lastCommentId: Int = 0

    // page cursor, last loaded time (epoch)
    private val lastLoadedTime = mutableMapOf<Int, Long>()

    override fun getRefreshKey(state: PagingState<Int, NoticeComment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorItem = state.closestItemToPosition(anchorPosition)
            anchorItem?.comment?.id
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NoticeComment> {
        return try {
            val cursor = lastCommentId.nextCursor
            assertPageCanBeLoaded(cursor)

            val response = noticeCommentRepository.getComment(noticeId, cursor)
            val (comments, _) = response.getOrThrow()

            updateLastLoadedTime(cursor)

            comments.maxOfOrNull { it.comment.id }?.let { maxCommentId ->
                lastCommentId = maxCommentId
            }
            LoadResult.Page(
                data = comments,
                prevKey = null, // Only paging forward
                nextKey = lastCommentId.nextCursor, // always have next key; fail if same load
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private val Int.nextCursor
        get() = this + 1

    private fun assertPageCanBeLoaded(key: Int) {
        if (!canBeLoaded(key)) {
            val message = "Page $key cannot be loaded."
            Timber.e(message)
            throw IllegalAccessException(message)
        }
    }

    private fun canBeLoaded(key: Int): Boolean {
        return if (key !in lastLoadedTime) {
            true
        } else {
            lastLoadedTime[key]!! + TIME_INTERVAL <= System.currentTimeMillis()
        }
    }

    private fun updateLastLoadedTime(key: Int) {
        lastLoadedTime[key] = System.currentTimeMillis()
    }

    override val keyReuseSupported: Boolean
        get() = true

    companion object {
        const val PAGE_SIZE = NoticeCommentRepository.PAGE_SIZE
        private const val TIME_INTERVAL = 3000L // ms
    }
}