package com.example.imageviewer.ui.download

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.imageviewer.R
import com.example.imageviewer.databinding.DownloadFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private val viewModel by viewModels<DownloadViewModel>()
    private lateinit var binding: DownloadFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DownloadFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backIcon.setOnClickListener {
//            activity?.onBackPressedDispatcher?.onBackPressed()
            findNavController().popBackStack()
        }


    }
}