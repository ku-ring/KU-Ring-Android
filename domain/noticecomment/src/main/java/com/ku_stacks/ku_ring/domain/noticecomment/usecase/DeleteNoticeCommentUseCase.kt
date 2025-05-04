package com.ku_stacks.ku_ring.domain.noticecomment.usecase

import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import javax.inject.Inject

class DeleteNoticeCommentUseCase @Inject constructor(
    private val noticeCommentRepository: NoticeCommentRepository,
) {
    /**
     * Deletes a comment. Any attempt to delete a comment that doesn't exist or whose notice doesn't exist will fail.
     *
     * @param noticeId The ID of the notice. Do not pass articleId here.
     * @param commentId The ID of the comment.
     *
     * @returns True if the comment is deleted successfully, false otherwise.
     */
    suspend operator fun invoke(noticeId: Int, commentId: Int): Result<Boolean> {
        return noticeCommentRepository.deleteComment(noticeId, commentId)
    }
}