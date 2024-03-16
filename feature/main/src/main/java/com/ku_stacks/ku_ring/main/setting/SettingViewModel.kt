package com.ku_stacks.ku_ring.main.setting

import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: PreferenceUtil
) : ViewModel() {

    private val _isExtNotificationAllowed = MutableStateFlow(pref.extNotificationAllowed)
    val isExtNotificationAllowed: StateFlow<Boolean>
        get() = _isExtNotificationAllowed

    fun setExtNotificationAllowed(value: Boolean) {
        pref.extNotificationAllowed = value
        _isExtNotificationAllowed.value = value
    }
}