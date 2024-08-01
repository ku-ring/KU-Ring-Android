package com.ku_stacks.ku_ring.main.survey

import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SurveyPopupViewModel @Inject constructor(
    private val preferences: PreferenceUtil,
) : ViewModel() {

    private val _isSurveyComplete = MutableStateFlow(true)
    val isSurveyComplete: StateFlow<Boolean>
        get() = _isSurveyComplete.asStateFlow()

    init {
        _isSurveyComplete.value = preferences.is2024SurveyComplete
    }

    fun onSurveyComplete() {
        preferences.is2024SurveyComplete = true
        _isSurveyComplete.value = true
    }
}