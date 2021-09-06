package com.sanislo.vodapp.presentation.metadata

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
import com.sanislo.vodapp.R
import com.sanislo.vodapp.databinding.FragmentMetadataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MetadataFragment : Fragment() {
    private lateinit var binding: FragmentMetadataBinding
    private val args: MetadataFragmentArgs by navArgs()
    private val viewModel: MetadataViewModel by viewModels()
    private val metadataAdapter = MetadataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadMetadata(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMetadataBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@MetadataFragment.viewModel
                binding = this
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        binding.rvMetadata.apply {
            setHasFixedSize(true)
            adapter = metadataAdapter
        }
        observeMetadata()
        observeActions()
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect {
                    when (it) {
                        is MetadataViewModel.Action.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun observeMetadata() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.metadataItems.collect {
                    metadataAdapter.submitList(it)
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
}