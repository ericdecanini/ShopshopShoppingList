package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var binding: FragmentHomeBinding

    private val shoppingLists = mutableListOf<ShoppingList>()
    private val adapter by lazy {
        ShoppingListAdapter(shoppingLists, viewModel.onShoppingListClick, findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        initClicks()
        initShoppingLists()
        observeState()

        return binding.root
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            updateShoppingLists(it.shoppingLists)
        })
    }

    private fun initShoppingLists() {
        binding.shoppingLists.adapter = adapter
        binding.shoppingLists.layoutManager = LinearLayoutManager(context)
    }

    private fun updateShoppingLists(lists: List<ShoppingList>) {
        this.shoppingLists.clear()
        this.shoppingLists.addAll(lists)
        adapter.notifyDataSetChanged()
    }

    private fun initClicks() {
        binding.fab.setOnClickListener { viewModel.navigateToListFragment(findNavController()) }
    }
}
