package com.ku_stacks.ku_ring.ui.main.setting

import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: PreferenceUtil
) : ViewModel() {

    fun isExtNotificationAllowed() = pref.extNotificationAllowed

    fun setExtNotificationAllowed(value: Boolean) {
        pref.extNotificationAllowed = value
    }
}