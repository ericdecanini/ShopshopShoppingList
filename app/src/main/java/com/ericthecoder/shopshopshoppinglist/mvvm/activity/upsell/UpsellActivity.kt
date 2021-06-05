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
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class UpsellActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UpsellViewModel::class.java)
    }

    private lateinit var binding: ActivityUpsellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upsell)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewmodel, viewModel)

        setSupportActionBar(binding.toolbar)
        observeViewEvents()
    }

    private fun observeViewEvents() = viewModel.viewEventLiveData.observe(this) { event ->
        when(event) {
            NavigateUp -> onBackPressed()
        }
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, UpsellActivity::class.java)
    }

}
