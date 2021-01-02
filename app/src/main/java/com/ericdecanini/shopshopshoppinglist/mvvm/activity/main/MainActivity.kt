package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.chaquo.python.Python
import com.ericdecanini.shopshopshoppinglist.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    logHelloWorld()
  }

  private fun logHelloWorld() {
    val python = Python.getInstance()
    val pythonFile = python.getModule("helloworld")
    val helloWorld: String = pythonFile.callAttr("hello_world").toString()
    Log.d("MainActivity", "Python result: $helloWorld")
    Toast.makeText(this, helloWorld, Toast.LENGTH_LONG).show()
  }
}
