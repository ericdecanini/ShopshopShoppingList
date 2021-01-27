package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentHomeBinding
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private lateinit var binding: FragmentHomeBinding

    private val shoppingLists = mutableListOf<ShoppingList>()
    private val adapter by lazy {
        ShoppingListAdapter(shoppingLists, viewModel.onShoppingListClick)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.setVariable(BR.viewmodel, viewModel)

        initShoppingLists()
        observeState()

        return binding.root
    }

    private fun initShoppingLists() {
        binding.shoppingLists.adapter = adapter
        binding.shoppingLists.layoutManager = LinearLayoutManager(context)
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            updateShoppingLists(it.shoppingLists)
        }
    }

    private fun updateShoppingLists(lists: List<ShoppingList>) {
        this.shoppingLists.clear()
        this.shoppingLists.addAll(lists)
        adapter.notifyDataSetChanged()
    }
}
