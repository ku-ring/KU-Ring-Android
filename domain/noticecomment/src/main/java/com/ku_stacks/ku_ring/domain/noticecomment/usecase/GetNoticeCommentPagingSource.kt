package com.ku_stacks.ku_ring.domain.noticecomment.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository

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

    override fun getRefreshKey(state: PagingState<Int, NoticeComment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorItem = state.closestItemToPosition(anchorPosition)
            anchorItem?.comment?.id
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NoticeComment> {
        return try {
            val response = noticeCommentRepository.getComment(noticeId, lastCommentId.nextCursor)
            val (comments, hasNext) = response.getOrThrow()

            comments.maxOfOrNull { it.comment.id }?.let { maxCommentId ->
                lastCommentId = maxCommentId
            }
            LoadResult.Page(
                data = comments,
                prevKey = null, // Only paging forward
                nextKey = if (hasNext) lastCommentId.nextCursor else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private val Int.nextCursor
        get() = this + 1

    companion object {
        const val PAGE_SIZE = NoticeCommentRepository.PAGE_SIZE
    }
}