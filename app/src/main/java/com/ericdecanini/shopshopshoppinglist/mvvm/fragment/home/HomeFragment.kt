package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        initClicks()

        return binding.root
    }

    private fun initClicks() {
        binding.fab.setOnClickListener { viewModel.navigateToListFragment(findNavController()) }
    }
}
