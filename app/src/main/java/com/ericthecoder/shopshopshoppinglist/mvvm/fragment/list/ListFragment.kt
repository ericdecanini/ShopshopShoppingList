package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.ericthecoder.shopshopshoppinglist.entities.extension.doNothing
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.ui.dialogs.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
    }

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var dialogNavigator: DialogNavigator
    @Inject lateinit var toastNavigator: ToastNavigator

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
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initList()
        observeState()
        observeEvents()
        inflateList(args.shoppingListId)

        return binding.root
    }

    private fun initList() {
        binding.shopList.adapter = adapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun observeState() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loaded -> renderShopItems(state.shoppingList.items)
                else -> doNothing()
            }
        }
    }

    private fun observeEvents() = viewModel.viewEvent.observe(viewLifecycleOwner) { event ->
        when (event) {
            NavigateUp -> findNavController().navigateUp()
            ClearFocus -> binding.root.clearFocus()
            is DisplayRenameDialog -> displayRenameDialog(event.listTitle, event.callback)
            is DisplayDeleteDialog -> displayDeleteDialog(event.listTitle, event.callback)
            is ShowToast -> toastNavigator.show(event.message)
        }
    }

    private fun displayRenameDialog(listTitle: String, callback: (String) -> Unit) {
        dialogNavigator.displayRenameDialog(listTitle, callback)
    }

    private fun displayDeleteDialog(listTitle: String, callback: () -> Unit) {
        dialogNavigator.displayGenericDialog(
            title = getString(R.string.delete),
            message = getString(R.string.delete_dialog_message, listTitle),
            positiveButton = getString(R.string.ok) to { callback() },
            negativeButton = getString(R.string.cancel) to { },
        )
    }

    private fun renderShopItems(items: List<ShopItem>) {
        val savedInstanceState = binding.shopList.layoutManager?.onSaveInstanceState()
        val diffResult = DiffUtil.calculateDiff(ShopItemDiffCallback(adapter.items, items))
        adapter.replaceItems(items)
        diffResult.dispatchUpdatesTo(adapter)

        binding.shopList.layoutManager?.onRestoreInstanceState(savedInstanceState)
    }

    private fun inflateList(id: Int) = viewModel.handleArgs(id)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.ic_delete -> viewModel.showDeleteDialog()
            R.id.ic_clear_checked -> viewModel.clearChecked()
            else -> return false
        }
        return true
    }
}
