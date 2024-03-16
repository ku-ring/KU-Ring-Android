package com.ku_stacks.ku_ring.main.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.FragmentNoticeBinding
import com.ku_stacks.ku_ring.main.notice.compose.NoticeScreen
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NoticesParentFragment : Fragment() {

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    lateinit var navigator: KuringNavigator

    private var _binding: FragmentNoticeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            KuringTheme {
                NoticeScreen(
                    onStorageIconClick = {
                        navigator.navigateToNoticeStorage(requireActivity())
                    },
                    onSearchIconClick = {
                        navigator.navigateToSearch(requireActivity())
                    },
                    onNotificationIconClick = {
                        navigator.navigateToNoticeStorage(requireActivity())
                    },
                    onNoticeClick = {
                        navigator.navigateToNoticeWeb(requireActivity(), it)
                    },
                    onNavigateToEditDepartment = {
                        navigator.navigateToEditSubscribedDepartment(requireActivity())
                    },
                )
            }
        }
        getFcmToken()
    }

    private fun getFcmToken() {
        lifecycleScope.launch {
            firebaseMessaging.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.e("Firebase instanceId fail : ${task.exception}")
                    return@addOnCompleteListener
                }
                val token = task.result
                pref.fcmToken = token
                Timber.e("FCM token : $token")
            }
        }
    }

    override fun onPause() {
        super.onPause()
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
