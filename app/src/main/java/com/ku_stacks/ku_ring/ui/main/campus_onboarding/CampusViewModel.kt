package com.ku_stacks.ku_ring.ui.main.campus_onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.repository.SendbirdRepository
import com.ku_stacks.ku_ring.ui_util.SingleLiveEvent
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.sendbird.android.SendbirdChat
import com.sendbird.android.params.UserUpdateParams
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class CampusViewModel @Inject constructor(
    private val pref: PreferenceUtil,
    private val repository: SendbirdRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val _finishEvent = SingleLiveEvent<String>()
    val finishEvent: LiveData<String>
        get() = _finishEvent

    init {
        Timber.e("CampusViewModel init")
    }

    fun connectToSendbird(userId: String, nickname: (String?) -> Unit) {
        SendbirdChat.connect(userId) { user, e ->
            if (e != null) {
                Timber.e("Sendbird connect error [${e.code}] : ${e.message}")
                _dialogEvent.postValue(R.string.chat_connect_error)
                return@connect
            }
            Timber.e("Sendbird connect success. nickname : ${user?.nickname}")

            pref.campusUserId = userId
            nickname(user?.nickname)
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
        val pattern = "^(?=.{2,15}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9가-힣._]+(?<![_.])\$"
        return Pattern.matches(pattern, nickname)
    }

    fun login(nickname: String) {
        authorizeNickname(nickname) {
            setNickname(nickname) {
                _finishEvent.postValue(nickname)
            }
        }
    }

    private fun authorizeNickname(nickname: String, isAuthorized: () -> Unit) {
        if (!isValidNicknameFormat(nickname)) {
            Timber.e("nickname: $nickname")
            _dialogEvent.postValue(R.string.nickname_not_valid_format)
            return
        }

        val userId = pref.campusUserId

        disposable.add(
            repository.hasDuplicateNickname(nickname, userId)
                .subscribe({
                    if (!it) {
                        isAuthorized()
                    } else {
                        _dialogEvent.postValue(R.string.nickname_duplicated)
                    }
                }, {
                    Timber.e("authorizeNickname error : $it")
                    _dialogEvent.postValue(R.string.nickname_check_error)
                })
        )
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