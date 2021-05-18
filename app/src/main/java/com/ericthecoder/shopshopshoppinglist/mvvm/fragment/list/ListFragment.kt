package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.adapter.ShopItemAdapter
import com.ericthecoder.shopshopshoppinglist.adapter.ShopItemDiffCallback
import com.ericthecoder.shopshopshoppinglist.databinding.FragmentListBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Loaded
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

    private var isNewList = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.setVariable(BR.viewmodel, viewModel)
        binding.setVariable(BR.newitem, binding.addItemEdit)
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initList()
        observeState()
        inflateList(args.shoppingListId)

        return binding.root
    }

    private fun initList() {
        binding.shopList.adapter = adapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun observeState() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Loaded -> { renderShopItems(state.shoppingList.items) }
                else -> { /* do nothing */ }
            }
        }
    }

    private fun renderShopItems(items: List<ShopItem>) {
        val savedInstanceState = binding.shopList.layoutManager?.onSaveInstanceState()
        val diffResult = DiffUtil.calculateDiff(ShopItemDiffCallback(adapter.items, items))
        adapter.replaceItems(items)
        diffResult.dispatchUpdatesTo(adapter)

        binding.shopList.layoutManager?.onRestoreInstanceState(savedInstanceState)

        if (isNewList) {
            isNewList = false
            viewModel.showRenameDialog()
        }
    }

    private fun inflateList(id: Int) =
        if (id != -1) {
            viewModel.loadShoppingList(id)
        } else {
            isNewList = true
            viewModel.createNewShoppingList()
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.ic_delete -> {
            viewModel.showDeleteDialog()
            true
        }
        else -> false
    }
}
