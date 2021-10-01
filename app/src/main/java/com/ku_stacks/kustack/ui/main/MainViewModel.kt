package com.ku_stacks.kustack.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(){

    init {
        Timber.e("MainViewModel injected")
    }

    fun sayHi(){
        Timber.e("HIHI")
    }
}