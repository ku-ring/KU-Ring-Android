package com.ku_stacks.ku_ring.domain.noticecomment.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import javax.inject.Inject

class GetNoticeCommentUseCase @Inject constructor(
    private val noticeCommentRepository: NoticeCommentRepository,
) {
    /**
     * Gets a comment. Comments will be paged by [NoticeCommentUseCase][com.ku_stacks.ku_ring.domain.noticecomment.usecase.GetNoticeCommentUseCase].
     *
     * @param noticeId The ID of the notice. Do not pass articleId here.
     *
     * @returns A list of comments.
     */
    operator fun invoke(noticeId: Int): Pager<Int, NoticeComment> =
        Pager(PagingConfig(GetNoticeCommentPagingSource.PAGE_SIZE)) {
            GetNoticeCommentPagingSource(noticeCommentRepository, noticeId)
        }
}