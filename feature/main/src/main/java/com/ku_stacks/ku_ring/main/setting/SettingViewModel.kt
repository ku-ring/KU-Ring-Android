package com.ku_stacks.ku_ring.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: PreferenceUtil,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _isExtNotificationAllowed = MutableStateFlow(pref.extNotificationAllowed)
    private val _userProfileState: MutableStateFlow<UserProfileState> =
        MutableStateFlow(UserProfileState.InitialLoading)

    val settingUiState: StateFlow<SettingUiState> = combine(
        _isExtNotificationAllowed,
        _userProfileState,
    ) { isExtNotificationAllowed, userProfileState ->
        SettingUiState(
            isExtNotificationAllowed = isExtNotificationAllowed,
            userProfileState = userProfileState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingUiState(
            isExtNotificationAllowed = pref.extNotificationAllowed,
            userProfileState = UserProfileState.InitialLoading,
        ),
    )

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

data class SettingUiState(
    val isExtNotificationAllowed: Boolean,
    val userProfileState: UserProfileState,
)

sealed class UserProfileState {
    data object InitialLoading : UserProfileState()
    data object NotLoggedIn : UserProfileState()
    data class LoggedIn(val nickname: String) : UserProfileState()
}
