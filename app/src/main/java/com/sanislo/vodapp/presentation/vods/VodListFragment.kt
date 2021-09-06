package com.sanislo.vodapp.presentation.vods

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
import com.sanislo.vodapp.databinding.FragmentVodListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VodListFragment : Fragment() {
    private lateinit var binding: FragmentVodListBinding

    private val viewModel: VodListViewModel by viewModels()
    private val vodListAdapter = VodListAdapter {
        viewModel.onClick(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetch()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentVodListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@VodListFragment.viewModel
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

    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_vod_list)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_watch_history -> {
                        findNavController().navigate(VodListFragmentDirections.actionVodListFragmentToHistoryFragment())
                        true
                    }
                    else -> false
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

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect {
                    when (it) {
                        is VodListViewModel.Action.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                        is VodListViewModel.Action.NavigateToPlayer -> navigateToPlayer(it)
                    }
                }
            }
        }
    }

    private fun navigateToPlayer(action: VodListViewModel.Action.NavigateToPlayer) {
        findNavController().navigate(
            VodListFragmentDirections.actionVodListFragmentToPlayerFragment(action.id)
        )
    }
}