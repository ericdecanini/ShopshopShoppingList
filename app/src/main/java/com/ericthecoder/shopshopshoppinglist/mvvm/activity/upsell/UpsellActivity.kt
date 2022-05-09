package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivityUpsellBinding
import com.ericthecoder.shopshopshoppinglist.library.extension.initColoredBackgroundAndStatusBar
import com.ericthecoder.shopshopshoppinglist.library.extension.setTheme
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.ShowSnackbar
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.ui.snackbar.SnackbarNavigator
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UpsellActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dialogNavigator: DialogNavigator

    @Inject
    lateinit var snackbarNavigator: SnackbarNavigator

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[UpsellViewModel::class.java]
    }

    private lateinit var binding: ActivityUpsellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(viewModel.getTheme())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upsell)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewmodel, viewModel)

        setSupportActionBar(binding.toolbar)
        initColoredBackgroundAndStatusBar(binding.root)
        observeViewEvents()
        observePurchaseResult()
    }

    private fun observeViewEvents() = viewModel.viewEvent.observe(this) { event ->
        when (event) {
            is UpsellViewModel.ViewEvent.ShowDialog -> showDialog(event.title, event.message, event.positiveButton)
            is ShowSnackbar -> showSnackbar(event.messageRes)
            NavigateUp -> onBackPressed()
        }
    }

    private fun showDialog(title: String, message: String, positiveButton: Pair<String, () -> Unit>) {
        dialogNavigator.displayGenericDialog(
            title = title,
            message = message,
            cancellable = false, // all dialogs in upsell should be non-cancellable
            positiveButton = positiveButton,
        )
    }

    private fun showSnackbar(@StringRes messageRes: Int) {
        snackbarNavigator.displaySnackbar(messageRes)
    }

    private fun observePurchaseResult() = viewModel.getPurchaseResultLiveData().observe(this) {
        viewModel.handlePurchaseResult(it)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, UpsellActivity::class.java)
    }

}
