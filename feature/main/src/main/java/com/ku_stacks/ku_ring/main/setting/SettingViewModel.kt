package com.ku_stacks.ku_ring.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.getHttpExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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
        when (userProfileState) {
            is UserProfileState.InitialLoading -> SettingUiState.Initial
            is UserProfileState.Error -> SettingUiState.Error
            is UserProfileState.NotLoggedIn,
            is UserProfileState.LoggedIn -> SettingUiState.Success(
                isExtNotificationEnabled = isExtNotificationAllowed,
                userProfileState = userProfileState,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingUiState.Initial,
    )

    fun setExtNotificationAllowed(value: Boolean) {
        pref.extNotificationAllowed = value
        _isExtNotificationAllowed.value = value
    }

    fun logout() = viewModelScope.launch {
        userRepository.logoutUser()
            .onSuccess { _userProfileState.update { UserProfileState.NotLoggedIn } }
            .onFailure(Timber::e)
    }

    fun getUserData() = viewModelScope.launch {
        userRepository.getUserData()
            .onSuccess { response ->
                _userProfileState.update { UserProfileState.LoggedIn(response.nickName) }
            }
            .onFailure { exception ->
                val message = exception.getHttpExceptionMessage()

                if (message.equals(NO_USER_MESSAGE)) {
                    _userProfileState.update { UserProfileState.NotLoggedIn }
                } else {
                    _userProfileState.update { UserProfileState.Error }
                }
            }
    }

    companion object {
        private const val NO_USER_MESSAGE = "계정을 찾을 수 없습니다."
    }
}

sealed class SettingUiState {
    data object Initial : SettingUiState()
    data object Error : SettingUiState()
    data class Success(
        val userProfileState: UserProfileState,
        val isExtNotificationEnabled: Boolean,
    ) : SettingUiState()
}

sealed class UserProfileState {
    data object InitialLoading : UserProfileState()
    data object Error : UserProfileState()
    data object NotLoggedIn : UserProfileState()
    data class LoggedIn(val nickname: String) : UserProfileState()
}
