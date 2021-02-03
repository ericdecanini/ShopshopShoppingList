package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import android.os.Bundle
import android.widget.Toast
import com.chaquo.python.Python
import com.ericdecanini.shopshopshoppinglist.R
import dagger.android.support.DaggerAppCompatActivity
import org.json.JSONArray

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    testDbThings()
  }

  private fun testDbThings() {
    val python = Python.getInstance()
    val pythonFile = python.getModule("commands")
    val commandsClass = pythonFile.callAttr("Commands")

    commandsClass.callAttr("insert_shoppinglist", "Test List")
    val getListsResult = commandsClass.callAttr("get_shoppinglists").toString()
    val resultJson = JSONArray(getListsResult)
    commandsClass.callAttr("cleanup")

    Toast.makeText(this, resultJson.toString(), Toast.LENGTH_LONG).show()
  }
}
