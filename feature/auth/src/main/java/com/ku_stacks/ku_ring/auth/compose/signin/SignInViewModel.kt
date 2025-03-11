package com.ku_stacks.ku_ring.auth.compose.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    //TODO: 로직 구현시 지워질 예정
    private var _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun updateId(id: String) = _id.update { id }
    fun updatePassword(password: String) = _password.update { password }
}