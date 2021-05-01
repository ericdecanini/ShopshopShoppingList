package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel by lazy {
    ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
  }

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this

    loadAd()
    viewModel.launchOnboardingIfNecessary()
  }

  private fun loadAd() {
    val adRequest = AdRequest.Builder().build()
    binding.adView.loadAd(adRequest)
  }

  companion object {

    fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  }
}
