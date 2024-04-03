package com.ku_stacks.ku_ring.main.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
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
                    onSearchIconClick = {
                        navigator.navigateToSearch(requireActivity())
                    },
                    onNotificationIconClick = {
                        navigator.navigateToEditSubscription(requireActivity())
                    },
                    onNoticeClick = {
                        navigator.navigateToNoticeWeb(requireActivity(), it)
                    },
                    onNavigateToEditDepartment = {
                        navigator.navigateToEditSubscribedDepartment(requireActivity())
                    },
                    modifier = Modifier
                        .background(KuringTheme.colors.background)
                        .fillMaxSize(),
                )
            }
        }
        getFcmToken()
    }

    private fun getFcmToken() {
        lifecycleScope.launch {
            firebaseMessaging.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = task.result
                pref.fcmToken = token
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
