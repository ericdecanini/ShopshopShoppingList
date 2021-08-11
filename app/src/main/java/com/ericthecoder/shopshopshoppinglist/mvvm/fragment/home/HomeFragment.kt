package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.FragmentHomeBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.extension.doNothing
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter.ShoppingListAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    @Inject lateinit var navigator: MainNavigator

    private lateinit var binding: FragmentHomeBinding

    private val shoppingLists = mutableListOf<ShoppingList>()
    private val adapter by lazy { ShoppingListAdapter(shoppingLists, viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.setVariable(BR.viewmodel, viewModel)
        binding.lifecycleOwner = this
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initShoppingLists()
        observeState()
        observeEvents()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshLists()
        viewModel.refreshPremiumState()
    }

    private fun initShoppingLists() {
        binding.shoppingLists.adapter = adapter
        binding.shoppingLists.layoutManager = LinearLayoutManager(context)
    }

    private fun observeState() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loaded -> updateShoppingLists(state.items)
                else -> doNothing()
            }
        }
    }

    private fun observeEvents() = viewModel.viewEvent.observe(viewLifecycleOwner) { event ->
        when (event) {
            is HomeViewModel.ViewEvent.SetHasOptionsMenu -> setHasOptionsMenu(event.enabled)
            HomeViewModel.ViewEvent.OpenNewList -> navigator.goToList()
            is HomeViewModel.ViewEvent.OpenList -> navigator.goToList(event.shoppingList)
            HomeViewModel.ViewEvent.OpenUpsell -> navigator.goToUpsell()
        }
    }

    private fun updateShoppingLists(lists: List<ShoppingList>) {
        this.shoppingLists.clear()
        this.shoppingLists.addAll(lists)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.ic_remove_ads -> {
            viewModel.navigateToUpsell()
            true
        }
        else -> false
    }
}
