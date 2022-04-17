package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.FragmentHomeBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.extension.doNothing
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter.ShoppingListAdapter
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    @Inject
    lateinit var navigator: Navigator

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
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar as Toolbar)

        viewModel.initTheme()
        initShoppingLists()
        observeState()
        observeEvents()
        setToolbarClickListener()

        return binding.root
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
            is SetHasOptionsMenu -> setHasOptionsMenu(event.enabled)
            is OpenList -> goToList(event.shoppingList)
            is SetThemeColor -> updateThemeColor(event.color)
            OpenUpsell -> navigator.goToUpsell()
        }
    }

    private fun updateShoppingLists(lists: List<ShoppingList>) {
        this.shoppingLists.clear()
        this.shoppingLists.addAll(lists)
        adapter.notifyDataSetChanged()
    }

    private fun goToList(shoppingList: ShoppingList? = null) {
        val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
        shoppingList?.let { action.shoppingListId = it.id }
        findNavController().navigate(action)
    }

    private fun updateThemeColor(color: Int) {
        binding.toolbar.setBackgroundColor(color)
    }

    private fun setToolbarClickListener() {
        binding.toolbar.setOnClickListener { viewModel.onToolbarClick() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshLists()
        viewModel.refreshPremiumState()
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
