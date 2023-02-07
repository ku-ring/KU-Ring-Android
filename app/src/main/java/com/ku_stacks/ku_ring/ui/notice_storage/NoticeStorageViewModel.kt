package com.ku_stacks.ku_ring.ui.notice_storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.data.mapper.toUiModel
import com.ku_stacks.ku_ring.di.IODispatcher
import com.ku_stacks.ku_ring.repository.SavedNoticeRepository
import com.ku_stacks.ku_ring.ui.notice_storage.ui_model.SavedNoticeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeStorageViewModel @Inject constructor(
    private val savedNoticeRepository: SavedNoticeRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _savedNotices = MutableStateFlow(emptyList<SavedNoticeUiModel>())
    val savedNotices: StateFlow<List<SavedNoticeUiModel>>
        get() = _savedNotices

    init {
        viewModelScope.launch(ioDispatcher) {
            savedNoticeRepository.getSavedNotices().collect { savedNotices ->
                _savedNotices.value = savedNotices
                    .map { it.toUiModel() }
                    .sortedByDescending { it.postedDate }
                Timber.d("Saved Notice Update: $savedNotices")
            }
        }
    }

    fun deleteNotice(articleId: String) {
        viewModelScope.launch(ioDispatcher) {
            savedNoticeRepository.deleteNotice(articleId)
            Timber.d("Notice $articleId deleted.")
        }
    }

    fun onClear() {
        viewModelScope.launch(ioDispatcher) {
            savedNoticeRepository.clearNotices()
        }
    }
}