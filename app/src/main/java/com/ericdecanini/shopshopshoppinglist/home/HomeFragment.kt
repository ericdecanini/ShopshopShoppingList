package com.ericdecanini.shopshopshoppinglist.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel>() {

    override fun getViewModelClass() = HomeViewModel::class.java

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
        binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.listFragment, null))
    }
}
