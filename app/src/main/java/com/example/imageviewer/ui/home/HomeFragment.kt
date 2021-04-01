package com.example.imageviewer.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageviewer.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var imageAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var binding: HomeFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding?.recyclerView ?: return
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.imageData.observe(viewLifecycleOwner) { images ->
            imageAdapter.images = images
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}