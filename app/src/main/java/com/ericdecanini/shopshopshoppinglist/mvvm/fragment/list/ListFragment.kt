package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseFragment
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentListBinding

class ListFragment : BaseFragment<ListViewModel>() {

    private lateinit var binding: FragmentListBinding

    private val shopItems = mutableListOf<ShopItem>()
    private val adapter by lazy {
        ShopItemAdapter(shopItems, viewModel.onItemUpdate, viewModel.onItemDelete)
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
        arguments?.let { autofill(it) }

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
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            renderView(it)
        }
    }

    private fun initList() {
        binding.shopList.adapter = adapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun renderView(state: ListViewState) {
        binding.title.setText(state.title)

        shopItems.clear()
        shopItems.addAll(state.list)
        binding.shopList.post { adapter.notifyDataSetChanged() }
    }

    private fun autofill(args: Bundle) {
        viewModel.loadShoppingList(args.getInt(KEY_LIST_ID))
    }

    companion object {
        const val KEY_LIST_ID = "KEY_LIST_ID"
    }
}
