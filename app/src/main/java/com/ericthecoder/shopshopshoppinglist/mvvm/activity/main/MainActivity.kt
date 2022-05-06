package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.NavGraphDirections
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivityMainBinding
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
import com.ericthecoder.shopshopshoppinglist.library.extension.getRootView
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainViewModel.ViewEvent.*
import com.ericthecoder.shopshopshoppinglist.ui.dialog.DialogNavigator
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.google.android.gms.ads.AdRequest
import com.google.android.material.color.MaterialColors
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var dialogNavigator: DialogNavigator

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(viewModel.getTheme())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewmodel, viewModel)

        loadAd()
        handleNestedNavigation()
        initBackgroundAndStatusBar()
        observeViewEvents()
        viewModel.launchOnboardingIfNecessary()
    }

    private fun setTheme(theme: Theme) {
        when (theme) {
            Theme.BLUE -> setTheme(R.style.Theme_ShopshopShoppingList_Blue)
            Theme.GREEN -> setTheme(R.style.Theme_ShopshopShoppingList_Green)
            Theme.ORANGE -> setTheme(R.style.Theme_ShopshopShoppingList_Orange)
            Theme.PINK -> setTheme(R.style.Theme_ShopshopShoppingList_Pink)
            Theme.PURPLE -> setTheme(R.style.Theme_ShopshopShoppingList_Purple)
            Theme.RED -> setTheme(R.style.Theme_ShopshopShoppingList_Red)
            Theme.YELLOW -> setTheme(R.style.Theme_ShopshopShoppingList_Yellow)
            Theme.DYNAMIC -> Unit
        }
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun handleNestedNavigation() {
        intent.extras?.let { extras ->
            (extras.getSerializable(KEY_NESTED_NAVIGATION_INSTRUCTION) as? NestedNavigationInstruction)
                ?.let {
                    viewModel.handleNestedInstruction(it)
                }
        }

        intent.removeExtra(KEY_NESTED_NAVIGATION_INSTRUCTION)
    }

    private fun initBackgroundAndStatusBar() {
        val backgroundColor = MaterialColors.getColor(getRootView(), R.attr.backgroundColor)
        val primaryColor = MaterialColors.getColor(getRootView(), R.attr.colorPrimary)
        val layeredColor = ColorUtils.blendARGB(backgroundColor, primaryColor, 0.05F)
        binding.root.setBackgroundColor(layeredColor)
        window.statusBarColor = layeredColor
    }

    private fun observeViewEvents() = viewModel.viewEvent.observe(this) { event ->
        when (event) {
            GoToList -> goToList()
            GoToOnboarding -> navigator.goToOnboarding()
            ShowPremiumPurchasedDialog -> showPremiumPurchasedDialog()
        }
    }

    private fun goToList() {
        val action = NavGraphDirections.actionOpenListFragment()
        findNavController(R.id.fragment_container_view).navigate(action)
    }

    private fun showPremiumPurchasedDialog() = dialogNavigator.displayGenericDialog(
        title = getString(R.string.purchase_dialog_purchased_title),
        message = getString(R.string.purchase_dialog_purchased_message),
        positiveButton = getString(R.string.ok) to {},
    )

    override fun onResume() {
        super.onResume()
        viewModel.fetchPremiumState()
    }

    companion object {

        private const val KEY_NESTED_NAVIGATION_INSTRUCTION = "KEY_NESTED_NAVIGATION_INSTRUCTION"

        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        fun getIntent(context: Context, nestedNavigationInstruction: NestedNavigationInstruction) =
            getIntent(context)
                .putExtra(KEY_NESTED_NAVIGATION_INSTRUCTION, nestedNavigationInstruction)
    }
}
