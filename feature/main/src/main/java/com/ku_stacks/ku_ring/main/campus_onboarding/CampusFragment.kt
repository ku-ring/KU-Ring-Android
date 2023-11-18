package com.ku_stacks.ku_ring.main.campus_onboarding

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.FragmentCampusBinding
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.ui_util.makeDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CampusFragment : Fragment() {

    private var _binding: FragmentCampusBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<CampusViewModel>()

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var navigator: KuringNavigator

    private val loginFinishResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            val account = task.getResult(ApiException::class.java)
            Timber.e("account email: ${account.email}")
            account.email?.let { userId ->
//                viewModel.connectToSendbird(userId) {}
                changeState(CampusState.SET_NICKNAME_STATE)
            }
        } catch (e: ApiException) {
            Timber.e("signInResult failed [${e.statusCode}]")
            requireContext().makeDialog(description = "구글 로그인에 실패하였습니다. [${e.statusCode}]")
        } catch (e: Exception) {
            Timber.e("signInResult failed : $e")
            requireContext().makeDialog(description = "구글 로그인에 실패하였습니다. ${e.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeEvent()
    }

    private fun setupView() {
        val campusUserId = pref.campusUserId
        if (campusUserId.isNotEmpty()) { // 저장된 ID 있음
            changeState(CampusState.AUTO_LOGIN_STATE)

            binding.campusAutoLoginLayout.campusNextBt.setOnClickListener {
//                viewModel.connectToSendbird(campusUserId) { nickname ->
//                    if (nickname != null) { // 닉네임도 있다면
//                        startChatActivity()
//                    } else { // 닉네임 설정해야한다면
//                        changeState(CampusState.SET_NICKNAME_STATE)
//                    }
//                }
            }
        } else { // 저장된 ID 없음. 처음인 경우
            changeState(CampusState.LOGIN_STATE)
            binding.campusLoginLayout.campusGoogleLoginBt.setOnClickListener {
                signInGoogle()
            }
        }
    }

    private fun observeEvent() {
        viewModel.dialogEvent.observe(viewLifecycleOwner) {
            requireContext().makeDialog(description = getString(it))
        }

        viewModel.finishEvent.observe(viewLifecycleOwner) {
            startChatActivity()
        }
    }

    private fun changeState(toNewState: CampusState) {
        Timber.e("changeState to $toNewState")
        when (toNewState) {
            CampusState.LOGIN_STATE -> {
                binding.campusLoginLayout.root.visibility = View.VISIBLE
                binding.campusSetNicknameLayout.root.visibility = View.GONE
                binding.campusAutoLoginLayout.root.visibility = View.GONE
            }

            CampusState.SET_NICKNAME_STATE -> {
                binding.campusLoginLayout.root.visibility = View.GONE
                binding.campusSetNicknameLayout.root.visibility = View.VISIBLE
                binding.campusAutoLoginLayout.root.visibility = View.GONE

                binding.campusSetNicknameLayout.nicknameEt.addTextChangedListener(object :
                    TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) = Unit

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) = Unit

                    override fun afterTextChanged(s: Editable?) {
                        val isValidFormat = viewModel.isValidNicknameFormat(s.toString())
                        val formatText = if (isValidFormat) {
                            R.string.nickname_valid_format
                        } else {
                            R.string.nickname_not_valid_format
                        }
                        val formatTextColor = if (isValidFormat) {
                            R.color.kus_gray
                        } else {
                            R.color.kus_pink
                        }

                        binding.campusSetNicknameLayout.nicknameFormatDescTv.apply {
                            text = getString(formatText)
                            setTextColor(ContextCompat.getColor(requireContext(), formatTextColor))
                        }
                    }
                })

                binding.campusSetNicknameLayout.campusNextBt.setOnClickListener {
                    val nickname = binding.campusSetNicknameLayout.nicknameEt.text.toString()
                    viewModel.login(nickname)
                }
            }

            CampusState.AUTO_LOGIN_STATE -> {
                binding.campusLoginLayout.root.visibility = View.GONE
                binding.campusSetNicknameLayout.root.visibility = View.GONE
                binding.campusAutoLoginLayout.root.visibility = View.VISIBLE
            }
        }
    }

    private fun signInGoogle() {
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient =
            GoogleSignIn.getClient(requireContext().applicationContext, googleSignInOptions)

        val intent = googleSignInClient.signInIntent
        loginFinishResult.launch(intent)
    }

    private fun startChatActivity() {
        changeState(CampusState.AUTO_LOGIN_STATE)

        navigator.navigateToChat(requireActivity())
        requireActivity().overridePendingTransition(
            R.anim.anim_slide_right_enter,
            R.anim.anim_stay_exit
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}