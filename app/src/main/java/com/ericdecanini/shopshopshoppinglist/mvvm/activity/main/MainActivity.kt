package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.ActivityMainBinding
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.lifecycleOwner = this

    loadAd()
    launchOnboardingFlow()
  }

  private fun loadAd() {
    val adRequest = AdRequest.Builder().build()
    binding.adView.loadAd(adRequest)
  }

  private fun launchOnboardingFlow() {
    val intent = Intent(this, OnboardingActivity::class.java)
    startActivity(intent)
  }
}
