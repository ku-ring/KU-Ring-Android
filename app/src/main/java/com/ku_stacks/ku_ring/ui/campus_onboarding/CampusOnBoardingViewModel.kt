package com.ku_stacks.ku_ring.ui.campus_onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class CampusOnBoardingViewModel @Inject constructor(

) : ViewModel() {

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val _finishEvent = SingleLiveEvent<String>()
    val finishEvent: LiveData<String>
        get() = _finishEvent

    fun isValidNicknameFormat(nickname: String): Boolean {
        val pattern = "^(?=.{5,15}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$"
        return Pattern.matches(pattern, nickname)
    }

    fun authorizeNickname(nickname: String) {
        if (!isValidNicknameFormat(nickname)) {
            Timber.e("nickname: $nickname")
            _dialogEvent.postValue(R.string.nickname_not_valid_format)
            return
        }

        _finishEvent.postValue(nickname)
    }
}