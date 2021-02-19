package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.FragmentListBinding
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
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

    private val adapter by lazy {
        ShopItemAdapter(mutableListOf(), viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.setVariable(BR.viewmodel, viewModel)
        binding.setVariable(BR.newitem, binding.addItemEdit)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initList()
        observeState()
        inflateList(args.shoppingListId)

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
        viewModel.shoppingListLiveData.observe(viewLifecycleOwner) {
            renderView(it)
        }
    }

    private fun renderView(shoppingList: ShoppingList) {
        binding.title.text = shoppingList.name

        val diffResult = DiffUtil.calculateDiff(ShopItemDiffCallback(adapter.items, shoppingList.items))
        adapter.replaceItems(shoppingList.items)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun inflateList(id: Int) {
        if (id != -1)
            viewModel.loadShoppingList(id)
        else
            viewModel.createNewShoppingList(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list, menu)
    }
}
