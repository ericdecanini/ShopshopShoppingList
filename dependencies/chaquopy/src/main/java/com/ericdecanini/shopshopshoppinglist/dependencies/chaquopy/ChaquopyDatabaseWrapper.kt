package com.ericdecanini.shopshopshoppinglist.dependencies.chaquopy

import com.chaquo.python.Python
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper

class ChaquopyDatabaseWrapper : PythonDatabaseWrapper {

    private val commands by lazy {
        Python.getInstance()
            .getModule("commands")
            .callAttr("Commands")
    }

    override fun getShoppingListsJson(): String {
//        insertSomeItems()
        return commands.callAttr("get_shoppinglists").toString()
    }

    override fun getShoppingListJsonById(id: Int) = commands.callAttr("get_shoppinglist_by_id").toString()

    override fun cleanup() {
        commands.callAttr("cleanup")
    }

    private fun insertSomeItems() {
        commands.callAttr("insert_shoppinglist", "Sample List")
        commands.callAttr("insert_shoppinglist", "Sample List")
        commands.callAttr("insert_shoppinglist", "Sample List")
    }
}

