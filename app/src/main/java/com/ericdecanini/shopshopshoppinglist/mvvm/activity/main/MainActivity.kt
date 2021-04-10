package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this

    loadAd()
  }

  private fun loadAd() {
    val adRequest = AdRequest.Builder().build()
    binding.adView.loadAd(adRequest)
  }
}
