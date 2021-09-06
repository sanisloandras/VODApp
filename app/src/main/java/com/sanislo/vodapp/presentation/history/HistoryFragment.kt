package com.sanislo.vodapp.presentation.history

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
import com.sanislo.vodapp.R
import com.sanislo.vodapp.databinding.FragmentHistoryBinding
import com.sanislo.vodapp.presentation.vods.VodListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private val vodListAdapter = VodListAdapter {
        viewModel.onClick(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHistoryBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@HistoryFragment.viewModel
                binding = this
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        binding.rvVod.apply {
            setHasFixedSize(true)
            adapter = vodListAdapter
        }
        observeVodList()
        observeActions()
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect {
                    when (it) {
                        is HistoryViewModel.Action.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                        is HistoryViewModel.Action.NavigateToPlayer -> navigateToPlayer(it)
                    }
                }
            }
        }
    }

    private fun observeVodList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vodList.collect {
                    vodListAdapter.submitList(it)
                }
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.arrow_left)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToPlayer(action: HistoryViewModel.Action.NavigateToPlayer) {
        findNavController().navigate(
            HistoryFragmentDirections.actionHistoryFragmentToPlayerFragment(
                action.id
            )
        )
    }
}