package com.sanislo.vodapp.presentation.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.*
import com.sanislo.vodapp.R
import com.sanislo.vodapp.databinding.FragmentPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    private val args: PlayerFragmentArgs by navArgs()
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var binding: FragmentPlayerBinding

    private val playbackStateListener: Player.Listener = playbackStateListener()
    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.load(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return FragmentPlayerBinding.inflate(inflater, container, false)
            .apply {
                binding = this
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePlaybackInfo()
        setInfoClickListener(view)
    }

    private fun setInfoClickListener(view: View) {
        view.findViewById<View>(R.id.ib_info).setOnClickListener {
            findNavController().navigate(
                PlayerFragmentDirections.actionPlayerFragmentToMetadataFragment(
                    args.id
                )
            )
        }
    }

    private fun observePlaybackInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playbackInfo.collect {
                    it?.also { (url, position) ->
                        initializePlayer(url, position)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop(args.id, player?.currentPosition)
        releasePlayer()
    }

    private fun initializePlayer(url: String, playbackPosition: Long) {
        player = SimpleExoPlayer.Builder(requireContext())
            .build().apply {
                binding.pvPlayerView.player = this
                val mediaItem = MediaItem.Builder()
                    .setUri(url)
                    .build()
                addMediaItem(mediaItem)
                playWhenReady = playWhenReady
                seekTo(playbackPosition)
                addListener(playbackStateListener)
                prepare()
                play()
            }
    }

    private fun releasePlayer() {
        player?.run {
            removeListener(playbackStateListener)
            release()
        }
        player = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == ExoPlayer.STATE_ENDED) {
                viewModel.onEnded(args.id)
                findNavController().popBackStack()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(requireContext(), "Player error. ${error.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}