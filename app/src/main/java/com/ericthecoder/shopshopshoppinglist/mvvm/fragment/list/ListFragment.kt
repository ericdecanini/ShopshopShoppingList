package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.*
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.FragmentListBinding
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.entities.extension.doNothing
import com.ericthecoder.shopshopshoppinglist.library.util.hideKeyboard
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewState.Loaded
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.adapter.ShopItemAdapter
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.adapter.ShopItemDiffCallback
import com.ericthecoder.shopshopshoppinglist.theme.ThemeViewModel
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.snackbar.SnackbarNavigator
import com.ericthecoder.shopshopshoppinglist.ui.toast.ToastNavigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.google.android.material.color.MaterialColors
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ListViewModel::class.java]
    }

    private val themeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ThemeViewModel::class.java]
    }

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var dialogNavigator: DialogNavigator

    @Inject
    lateinit var toastNavigator: ToastNavigator

    @Inject
    lateinit var snackbarNavigator: SnackbarNavigator

    private lateinit var binding: FragmentListBinding
    private val args: ListFragmentArgs by navArgs()
    private val adapter by lazy {
        ShopItemAdapter(mutableListOf(), themeViewModel.getTheme(), viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.setVariable(BR.viewmodel, viewModel)
        binding.setVariable(BR.newitem, binding.addItemEdit)
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        initList()
        configureAddItemField()
        observeState()
        observeEvents()
        inflateList(args.shoppingListId)

        return binding.root
    }

    private fun initList() {
        binding.shopList.adapter = adapter
        binding.shopList.layoutManager = LinearLayoutManager(context)
        binding.shopList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        setupItemTouchHelper()
    }

    private fun setupItemTouchHelper() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.moveItem(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeItem(position)
            }
        }
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(binding.shopList)
    }

    private fun configureAddItemField() {
        val colorAnimator = createColorAnimator()
        binding.addItemEdit.addTextChangedColorChanging(colorAnimator)
    }

    private fun createColorAnimator() = ObjectAnimator.ofObject(
        binding.addItemButton,
        "colorFilter",
        ArgbEvaluator(),
        0, 0,
    ).setDuration(200)

    private fun TextInputEditText.addTextChangedColorChanging(animator: ObjectAnimator) {
        val colorPrimary = MaterialColors.getColor(binding.root, R.attr.colorPrimary)
        val colorSecondary = MaterialColors.getColor(binding.root, R.attr.colorSecondary)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                val currentColor = animator.animatedValue
                    ?: if (text.isBlank()) colorPrimary else colorSecondary
                animator.setObjectValues(currentColor, if (text.isBlank()) colorSecondary else colorPrimary)
                animator.start()
            }

            override fun afterTextChanged(s: Editable) = Unit
        })
    }

    private fun observeState() {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loaded -> renderShoppingList(state.shoppingList)
                else -> doNothing()
            }
        }
    }

    private fun observeEvents() = viewModel.viewEvent.observe(viewLifecycleOwner) { event ->
        when (event) {
            NavigateUp -> findNavController().navigateUp()
            ClearFocus -> binding.root.clearFocus()
            ClearEditText -> binding.addItemEdit.setText("")
            HideKeyboard -> hideKeyboard(binding.root)
            is DisplayGenericDialog -> displayGenericDialog(event.title, event.message)
            is DisplayNewListDialog -> displayNewListDialog(event.onNameSet)
            is DisplayRenameDialog -> displayRenameDialog(event.listTitle, event.callback)
            is DisplayDeleteDialog -> displayDeleteDialog(event.listTitle, event.callback)
            is ShowToast -> toastNavigator.show(event.message)
            is Share -> share(event.text)
            is ShowUndoRemoveItemSnackbar -> showUndoRemoveSnackbar(event.item, event.position)
        }
    }

    private fun displayGenericDialog(title: String, message: String) {
        dialogNavigator.displayGenericDialog(
            title = title,
            message = message,
            positiveButton = getString(R.string.ok) to { }
        )
    }

    private fun displayNewListDialog(callback: (String) -> Unit) {
        dialogNavigator.displayRenameDialog(
            getString(R.string.header_new_list),
            "",
            callback
        )
    }

    private fun displayRenameDialog(listTitle: String, callback: (String) -> Unit) {
        dialogNavigator.displayRenameDialog(
            getString(R.string.rename),
            listTitle,
            callback
        )
    }

    private fun displayDeleteDialog(listTitle: String, callback: () -> Unit) {
        dialogNavigator.displayGenericDialog(
            title = getString(R.string.delete),
            message = getString(R.string.delete_dialog_message, listTitle),
            positiveButton = getString(R.string.ok) to { callback() },
            negativeButton = getString(R.string.cancel) to { },
        )
    }

    private fun share(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun showUndoRemoveSnackbar(removedItem: ShopItem, position: Int) {
        snackbarNavigator.displaySnackbar(
            message = getString(R.string.deleted_item, removedItem.name),
            ctaText = getString(R.string.undo),
            ctaCallback = { viewModel.reAddItem(removedItem, position) }
        )
    }

    private fun renderShoppingList(shoppingList: ShoppingList) {
        binding.title.text = shoppingList.name
        renderShopItems(shoppingList.items)
    }

    private fun renderShopItems(items: List<ShopItem>) {
        val diffResult = DiffUtil.calculateDiff(ShopItemDiffCallback(adapter.items, items))
        adapter.replaceItems(items)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun inflateList(id: Int) = viewModel.loadShoppingList(id)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_delete -> viewModel.showDeleteDialog()
            R.id.ic_clear_checked -> viewModel.clearCheckedItems()
            R.id.ic_share -> viewModel.onShareButtonClicked()
            else -> return false
        }
        return true
    }
}
