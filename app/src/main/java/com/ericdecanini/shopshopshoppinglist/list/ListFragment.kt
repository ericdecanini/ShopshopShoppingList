package com.ericdecanini.shopshopshoppinglist.list

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
import com.ericdecanini.shopshopshoppinglist.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentListBinding

class ListFragment : BaseFragment<ListViewModel>() {

    override fun getViewModelClass() = ListViewModel::class.java

    private lateinit var binding: FragmentListBinding

    private val shopItems = mutableListOf<ShopItem>()
    private val shopListAdapter = ShopItemAdapter(shopItems)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        initClicks()
        initList()
        observeList()

        return binding.root
    }

    private fun initClicks() {
        binding.addItemButton.setOnClickListener {
            viewModel.onAddItemClick(binding.addItemEdit.text.toString())
        }
    }

    private fun observeList() {
        viewModel.listLiveData.observe(viewLifecycleOwner, Observer { items ->
            shopItems.clear()
            shopItems.addAll(items)
            shopListAdapter.notifyDataSetChanged()
        })
    }

    private fun initList() {
        binding.shopList.adapter = shopListAdapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}
