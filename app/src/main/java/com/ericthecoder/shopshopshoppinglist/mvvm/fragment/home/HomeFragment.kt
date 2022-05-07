package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.FragmentHomeBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewState.Search
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter.ShoppingListAdapter
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.google.android.material.elevation.SurfaceColors
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dialogNavigator: DialogNavigator

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    @Inject
    lateinit var navigator: Navigator

    private lateinit var binding: FragmentHomeBinding

    private val shoppingLists = mutableListOf<ShoppingList>()
    private val adapter by lazy { ShoppingListAdapter(shoppingLists, viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.setVariable(BR.viewmodel, viewModel)
        binding.lifecycleOwner = this

        initSearchBar()
        initShoppingLists()
        observeState()
        observeEvents()

        return binding.root
    }

    private fun initSearchBar() {
        val surface4 = SurfaceColors.SURFACE_4.getColor(requireContext())
        binding.searchBarLayout.backgroundTintList = ColorStateList.valueOf(surface4)

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchBarTextChanged(s?.toString().orEmpty())
                binding.splashNoSearchResultsText.text = getString(R.string.splash_home_no_search_results, s)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun initShoppingLists() {
        binding.shoppingLists.adapter = adapter
        binding.shoppingLists.layoutManager = LinearLayoutManager(context)
    }

    private fun observeState() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loaded -> updateShoppingLists(state.items)
                is Search -> updateShoppingLists(state.items)
                is HomeViewState.Error,
                HomeViewState.Initial,
                HomeViewState.Loading -> Unit
            }
        }
    }

    private fun updateShoppingLists(lists: List<ShoppingList>) {
        this.shoppingLists.clear()
        this.shoppingLists.addAll(lists)
        adapter.notifyDataSetChanged()
    }

    private fun observeEvents() = viewModel.viewEvent.observe(viewLifecycleOwner) { event ->
        when (event) {
            is SetUpsellButtonVisible -> setUpsellButtonVisible(event.visible)
            is OpenList -> goToList(event.shoppingList)
            OpenUpsell -> navigator.goToUpsell()
            OpenThemeDialog -> openThemeDialog()
            RecreateActivity -> activity?.recreate()
        }
    }

    private fun setUpsellButtonVisible(visible: Boolean) {
        binding.upgradeToPremiumButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun goToList(shoppingList: ShoppingList? = null) {
        val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
        shoppingList?.let { action.shoppingListId = it.id }
        findNavController().navigate(action)
    }

    private fun openThemeDialog() {
        dialogNavigator.displayThemeDialog { viewModel.switchToTheme(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshLists()
        viewModel.refreshPremiumState()
    }
}
