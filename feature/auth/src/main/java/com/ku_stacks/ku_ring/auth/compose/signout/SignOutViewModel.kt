package com.ku_stacks.ku_ring.auth.compose.signout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _sideEffect = Channel<SignOutSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun signOut() = viewModelScope.launch {
        userRepository.withdrawUser()
            .onSuccess {
                _sideEffect.send(SignOutSideEffect.NavigateToSignOutComplete)
            }.onFailure(Timber::e)
    }
}