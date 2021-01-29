package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentListBinding
import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.ListViewState
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private lateinit var binding: FragmentListBinding
    private val args: ListFragmentArgs by navArgs()

    private val shopItems = mutableListOf<ShopItem>()
    private val adapter by lazy {
        ShopItemAdapter(shopItems, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.setVariable(BR.viewmodel, viewModel)
        binding.setVariable(BR.newitem, binding.addItemEdit)

        initList()
        observeState()
        autofill(args.shoppingListId)

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

    private fun initList() {
        binding.shopList.adapter = adapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            renderView(it)
        }
    }

    private fun renderView(state: ListViewState) {
        binding.title.setText(state.title)

        shopItems.clear()
        shopItems.addAll(state.list)
        binding.shopList.post { adapter.notifyDataSetChanged() }
    }

    private fun autofill(id: Int) {
        if (id != -1) { viewModel.loadShoppingList(id) }
    }
}
