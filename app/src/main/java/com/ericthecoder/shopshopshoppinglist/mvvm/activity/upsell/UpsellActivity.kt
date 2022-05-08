package com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivityUpsellBinding
import com.ericthecoder.shopshopshoppinglist.library.extension.initColoredBackgroundAndStatusBar
import com.ericthecoder.shopshopshoppinglist.library.extension.setTheme
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.upsell.UpsellViewModel.ViewEvent.NavigateUp
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
