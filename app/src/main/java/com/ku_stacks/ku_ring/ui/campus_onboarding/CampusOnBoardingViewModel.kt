package com.ku_stacks.ku_ring.ui.campus_onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.sendbird.android.SendbirdChat
import com.sendbird.android.params.UserUpdateParams
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class CampusOnBoardingViewModel @Inject constructor(
    private val pref: PreferenceUtil
) : ViewModel() {

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val _finishEvent = SingleLiveEvent<String>()
    val finishEvent: LiveData<String>
        get() = _finishEvent

    init {
        pref.fcmToken?.let { token ->
            // TODO : fcmToken 은 userId의 길이 제한보다 길어서 사용할 수 없음. 임시적으로 fcmToken 앞의 4글자만 사용
            SendbirdChat.connect(token.substring(0, 4)) { user, e ->
                if (e != null) {
                    Timber.e("Sendbird connect error [${e.code}] : ${e.message}")
                    return@connect
                }
                Timber.e("Sendbird connect success. nickname : ${user?.nickname}")
            }
        }
    }

    /*
    (?=.{5,15}$) // 5-15 characters long
    (?![_.]) // no _ or . at the beginning
    (?!.*[_.]{2}) // no __ or _. or ._ or .. inside
    [a-zA-Z0-9가-힣._] // allowed characters
    (?<![_.]) // no _ or . at the end
     */
    fun isValidNicknameFormat(nickname: String): Boolean {
        val pattern = "^(?=.{5,15}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9가-힣._]+(?<![_.])\$"
        return Pattern.matches(pattern, nickname)
    }

    fun login(nickname: String) {
        if (authorizeNickname(nickname)) {
            setNickname(nickname) {
                _finishEvent.postValue(nickname)
            }
        }
    }

    private fun authorizeNickname(nickname: String): Boolean {
        if (!isValidNicknameFormat(nickname)) {
            Timber.e("nickname: $nickname")
            _dialogEvent.postValue(R.string.nickname_not_valid_format)
            return false
        }

        // TODO : 중복 닉네임 체크
        return true
    }

    private fun setNickname(nickname: String, isDone: () -> Unit) {
        val params = UserUpdateParams()
            .setNickname(nickname)

        SendbirdChat.updateCurrentUserInfo(params) { e ->
            if (e != null) {
                Timber.e("updateCurrentUserInfo error [${e.code} : ${e.message}]")
                return@updateCurrentUserInfo
            }
            Timber.e("updateCurrentUserInfo success")
            isDone()
        }
    }
}