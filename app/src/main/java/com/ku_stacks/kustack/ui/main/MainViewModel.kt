package com.ku_stacks.kustack.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(){

    init {
        Log.e("TAG", "MainViewModel injected")
    }

    fun sayHi(){
        Log.e("TAG","HIHI")
    }
}