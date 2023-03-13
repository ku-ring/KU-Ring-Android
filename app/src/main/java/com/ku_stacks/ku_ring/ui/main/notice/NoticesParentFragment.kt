package com.ku_stacks.ku_ring.ui.main.notice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentNoticeBinding
import com.ku_stacks.ku_ring.ui.my_notification.NotificationActivity
import com.ku_stacks.ku_ring.ui.notice_storage.NoticeStorageActivity
import com.ku_stacks.ku_ring.util.PreferenceUtil
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

    private var _binding: FragmentNoticeBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<NoticeViewModel>()

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

        setupHeader()
        observeData()
        getFcmToken()
    }

    private fun setupHeader() {
        val pagerAdapter = NoticesPagerAdapter(childFragmentManager, lifecycle)
        binding.homeViewpager.adapter = pagerAdapter

        TabLayoutMediator(
            binding.mainHeader.tabLayout,
            binding.homeViewpager,
            true
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.bachelor)
                1 -> tab.text = getString(R.string.scholarship)
                2 -> tab.text = getString(R.string.employ)
                3 -> tab.text = getString(R.string.nation)
                4 -> tab.text = getString(R.string.student)
                5 -> tab.text = getString(R.string.industry)
                6 -> tab.text = getString(R.string.normal)
                7 -> tab.text = getString(R.string.library)
            }
        }.attach()

        binding.mainHeader.inventoryImg.setOnClickListener {
            val intent = Intent(requireActivity(), NoticeStorageActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(
                R.anim.anim_slide_right_enter,
                R.anim.anim_stay_exit
            )
        }

        binding.mainHeader.bellImg.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(
                R.anim.anim_slide_right_enter,
                R.anim.anim_stay_exit
            )
        }
    }

    private fun observeData() {
        viewModel.pushCount.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    binding.mainHeader.notiCountBt.visibility = View.GONE
                }
                in 1..99 -> {
                    binding.mainHeader.notiCountBt.visibility = View.VISIBLE
                    binding.mainHeader.notiCountBt.text = it.toString()
                }
                else -> {
                    binding.mainHeader.notiCountBt.visibility = View.VISIBLE
                    binding.mainHeader.notiCountBt.text =
                        getString(R.string.push_notification_max_count)
                }
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}