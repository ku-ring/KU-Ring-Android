package com.ku_stacks.ku_ring.ui.feedback

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class FeedbackViewModel @Inject constructor() : ViewModel() {

    private val disposable = CompositeDisposable()

    init {
        Timber.e("FeedbackViewModel injected")
    }
}