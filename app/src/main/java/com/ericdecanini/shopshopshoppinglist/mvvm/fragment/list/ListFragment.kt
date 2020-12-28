package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentListBinding

class ListFragment : BaseFragment<ListViewModel>() {

    private lateinit var binding: FragmentListBinding

    private val shopItems = mutableListOf<ShopItem>()
    private val adapter by lazy {
        ShopItemAdapter(shopItems, viewModel.onItemNameChanged)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        initClicks()
        initList()
        observeState()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter.saveStates(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { adapter.restoreStates(it) }
    }

    private fun initClicks() {
        binding.addItemButton.setOnClickListener {
            viewModel.onAddItemClick(binding.addItemEdit.text.toString())
            binding.addItemEdit.text = null
        }
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            updateList(it.list)
        })
    }

    private fun initList() {
        binding.shopList.adapter = adapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun updateList(items: List<ShopItem>) {
        shopItems.clear()
        shopItems.addAll(items)
        adapter.notifyDataSetChanged()
    }
}
