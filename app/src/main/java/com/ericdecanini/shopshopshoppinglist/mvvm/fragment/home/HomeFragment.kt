package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var binding: FragmentHomeBinding

    private val lists = mutableListOf<ShoppingList>()
    private val adapter by lazy {
        ShoppingListAdapter(lists)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        initClicks()
        initList()
        observeState()

        return binding.root
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            updateLists(it.lists)
        })
    }

    private fun initList() {
        binding.lists.adapter = adapter
        binding.lists.layoutManager = LinearLayoutManager(context)
    }

    private fun updateLists(lists: List<ShoppingList>) {
        this.lists.clear()
        this.lists.addAll(lists)
        adapter.notifyDataSetChanged()
    }

    private fun initClicks() {
        binding.fab.setOnClickListener { viewModel.navigateToListFragment(findNavController()) }
    }
}
