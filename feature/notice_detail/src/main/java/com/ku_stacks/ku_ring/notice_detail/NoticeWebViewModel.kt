package com.ku_stacks.ku_ring.notice_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.domain.noticecomment.usecase.CreateNoticeCommentUseCase
import com.ku_stacks.ku_ring.domain.noticecomment.usecase.DeleteNoticeCommentUseCase
import com.ku_stacks.ku_ring.domain.noticecomment.usecase.GetNoticeCommentUseCase
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.util.suspendRunCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeWebViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val createNoticeCommentUseCase: CreateNoticeCommentUseCase,
    private val deleteNoticeCommentUseCase: DeleteNoticeCommentUseCase,
    private val getNoticeCommentUseCase: GetNoticeCommentUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val webViewNotice: WebViewNotice? by lazy { savedStateHandle[WebViewNotice.EXTRA_KEY] }

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean>
        get() = _isSaved

    private val _commentsPager = MutableStateFlow<Pager<Int, NoticeComment>?>(null)
    val commentsPager = _commentsPager.asStateFlow()

    private val _replyCommentId = MutableStateFlow<Int?>(null)
    /**
     * Reply if not null, otherwise a common comment.
     */
    val replyCommentId = _replyCommentId.asStateFlow()

    private val _deleteCommentId = MutableStateFlow<Int?>(null)
    val deleteCommentId = _deleteCommentId.asStateFlow()

    init {
        viewModelScope.launch {
            noticeRepository.getSavedNotices().collect { savedNotices ->
                _isSaved.value = savedNotices.any { it.articleId == webViewNotice?.articleId }
            }
        }
    }

    fun updateNoticeTobeRead(webViewNotice: WebViewNotice) {
        viewModelScope.launch {
            suspendRunCatching {
                noticeRepository.updateNoticeToBeReadOnStorage(
                    webViewNotice.articleId,
                    webViewNotice.category
                )
                noticeRepository.updateNoticeToBeRead(
                    webViewNotice.articleId,
                    webViewNotice.category
                )
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    fun onSaveButtonClick() {
        if (webViewNotice == null) return
        viewModelScope.launch {
            noticeRepository.updateSavedStatus(
                webViewNotice?.articleId.orEmpty(),
                webViewNotice?.category.orEmpty(),
                !isSaved.value
            )
        }
    }

    fun onCommentBottomSheetOpen() {
        webViewNotice?.id?.let { id ->
            if (commentsPager.value == null) {
                _commentsPager.value = getNoticeCommentUseCase(id)
            }
        }
    }

    fun createComment(
        comment: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        webViewNotice?.id?.let { id ->
            viewModelScope.launch {
                createNoticeCommentUseCase(id, replyCommentId.value, comment)
                    .onSuccess { onSuccess() }
                    .onFailure { onFail() }
            }
        }
    }

    fun setReplyCommentId(id: Int?) {
        _replyCommentId.value = id
    }

    fun showDeleteCommentPopup(commentId: Int) {
        setDeleteCommentId(commentId)
    }

    fun hideDeleteCommentPopup() {
        setDeleteCommentId(null)
    }

    private fun setDeleteCommentId(commentId: Int?) {
        _deleteCommentId.value = commentId
    }

    fun deleteComment(
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        val noticeId = webViewNotice?.id
        val deleteCommentId = deleteCommentId.value
        if (noticeId != null && deleteCommentId != null) {
            viewModelScope.launch {
                deleteNoticeCommentUseCase(noticeId, deleteCommentId)
                    .onSuccess { onSuccess() }
                    .onFailure { onFail() }
            }
        }
    }
}