package com.ku_stacks.ku_ring.domain.noticecomment.usecase

import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import javax.inject.Inject

class CreateNoticeCommentUseCase @Inject constructor(
    private val noticeCommentRepository: NoticeCommentRepository,
) {
    /**
     * Creates a new comment.
     *
     * @param noticeId The ID of the notice. Do not pass articleId here.
     * @param parentCommentId The ID of the parent comment. If not null, this comment will be considered as a reply comment.
     * @param content The content of the comment.
     *
     * @returns True if the comment is created successfully, false otherwise.
     */
    suspend operator fun invoke(
        noticeId: Int,
        parentCommentId: Int?,
        content: String,
    ): Result<Unit> {
        return noticeCommentRepository.createComment(noticeId, parentCommentId, content)
    }
}