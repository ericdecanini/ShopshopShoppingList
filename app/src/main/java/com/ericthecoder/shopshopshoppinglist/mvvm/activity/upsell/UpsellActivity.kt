package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivityUpsellBinding
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
import com.ericthecoder.shopshopshoppinglist.theme.Theme
import com.ericthecoder.shopshopshoppinglist.theme.ThemeViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UpsellActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[UpsellViewModel::class.java]
    }

    private val themeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ThemeViewModel::class.java]
    }

    private lateinit var binding: ActivityUpsellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upsell)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewmodel, viewModel)

        setSupportActionBar(binding.toolbar)
        observeTheme()
        observeViewEvents()
        observePurchaseResult()
    }

    private fun observeTheme() = themeViewModel.theme.observe(this) { theme ->
        setTheme(theme)
    }

    private fun setTheme(theme: Theme) {
//        val themeColor = getColor(theme.colorRes)
//
//        binding.toolbar.setBackgroundColor(themeColor)
//        window.statusBarColor = themeColor
//        binding.colorScrimOne.backgroundTintList = ColorStateList.valueOf(themeColor)
//        binding.colorScrimTwo.backgroundTintList = ColorStateList.valueOf(themeColor)
//        binding.imageBoxOneTextbox.setBackgroundColor(themeColor)
//        binding.imageBoxTwoTextbox.setBackgroundColor(themeColor)
    }

    private fun observeViewEvents() = viewModel.viewEventLiveData.observe(this) { event ->
        when (event) {
            NavigateUp -> onBackPressed()
        }
    }

    private fun observePurchaseResult() = viewModel.getPurchaseResultLiveData().observe(this) {
        viewModel.handlePurchaseResult(it)
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, UpsellActivity::class.java)
    }

}
