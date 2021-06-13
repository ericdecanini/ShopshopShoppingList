package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
    handleIntent()
  }

  override fun onResume() {
    super.onResume()
    viewModel.fetchPremiumState()
  }

  private fun handleIntent() {
    intent.extras?.let { extras ->
      (extras.getSerializable(KEY_NESTED_NAVIGATION_INSTRUCTION) as? NestedNavigationInstruction)
        ?.let {
          viewModel.handleNestedInstruction(it)
        }
    }
  }

  private fun loadAd() {
    val adRequest = AdRequest.Builder().build()
    binding.adView.loadAd(adRequest)
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
