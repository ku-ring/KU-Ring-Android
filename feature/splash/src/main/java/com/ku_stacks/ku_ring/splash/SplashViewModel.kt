package com.ku_stacks.ku_ring.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.space.repository.KuringSpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val kuringSpaceRepository: KuringSpaceRepository
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow(SplashScreenState.INITIAL)
    val splashScreenState: StateFlow<SplashScreenState> = _splashScreenState.asStateFlow()

    fun checkUpdateRequired(currentVersion: String) {
        _splashScreenState.value = SplashScreenState.LOADING
        viewModelScope.launch {
            _splashScreenState.value = getAppVersionState(currentVersion)
        }
    }

    private suspend fun getAppVersionState(currentVersion: String): SplashScreenState {
        return if (isAppVersionDeprecated(currentVersion)) SplashScreenState.UPDATE_REQUIRED else SplashScreenState.UPDATE_NOT_REQUIRED
    }

    private suspend fun isAppVersionDeprecated(currentVersion: SemVer): Boolean {
        val minimumVersion = getMinimumVersion().let {
            SemVer.parse(it)
        }
        return currentVersion < minimumVersion
    }

    private suspend fun getMinimumVersion(): String {
        return try {
            kuringSpaceRepository.getMinimumAppVersion()
        } catch (e: Exception) {
            "0.0.0"
        }
    }

    fun dismissUpdateNotification() {
        _splashScreenState.value = SplashScreenState.DISMISS_UPDATE
    }
}