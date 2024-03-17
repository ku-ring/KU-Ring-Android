package com.ku_stacks.ku_ring.main.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.ku_stacks.ku_ring.designsystem.theme.Background
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.archive.compose.ArchiveScreen
import com.ku_stacks.ku_ring.main.databinding.FragmentArchiveBinding
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArchiveFragment : Fragment() {
    @Inject
    lateinit var navigator: KuringNavigator

    private var _binding: FragmentArchiveBinding? = null
    private val binding: FragmentArchiveBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setComposable()
    }

    private fun setComposable() {
        binding.composeView.setContent {
            KuringTheme {
                ArchiveScreen(
                    onNoticeClick = ::startNoticeActivity,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background),
                )
            }
        }
    }

    private fun startNoticeActivity(notice: Notice) {
        navigator.navigateToNoticeWeb(requireActivity(), notice)
    }

}