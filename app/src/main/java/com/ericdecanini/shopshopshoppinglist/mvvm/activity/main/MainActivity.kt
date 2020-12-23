package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.os.Bundle
import com.ericdecanini.shopshopshoppinglist.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
