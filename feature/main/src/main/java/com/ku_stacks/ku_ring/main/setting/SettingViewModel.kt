package com.ku_stacks.ku_ring.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: PreferenceUtil,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _userProfileState: MutableStateFlow<UserProfileState> =
        MutableStateFlow(UserProfileState.InitialLoading)
    val userProfileState: StateFlow<UserProfileState> = _userProfileState.asStateFlow()

    private val _isExtNotificationAllowed = MutableStateFlow(pref.extNotificationAllowed)
    val isExtNotificationAllowed: StateFlow<Boolean>
        get() = _isExtNotificationAllowed

    init {
        getUserData()
    }

    fun setExtNotificationAllowed(value: Boolean) {
        pref.extNotificationAllowed = value
        _isExtNotificationAllowed.value = value
    }

    fun logout() = viewModelScope.launch {
        //TODO: 로그아웃 API 연결
    }

    fun getUserData() = viewModelScope.launch {
        //TODO: 유저 정보 API 연결
        updateUserProfileState()
    }

    private fun updateUserProfileState(nickName: String? = null) = _userProfileState.update {
        if (nickName == null) UserProfileState.NotLoggedIn
        else UserProfileState.LoggedIn(nickName)
    }
}

sealed class UserProfileState {
    data object InitialLoading : UserProfileState()
    data object NotLoggedIn : UserProfileState()
    data class LoggedIn(val nickname: String) : UserProfileState()
}