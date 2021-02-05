package com.ericdecanini.shopshopshoppinglist.dependencies.chaquopy

import com.chaquo.python.Python
import com.ericdecanini.shopshopshoppinglist.usecases.database.PythonDatabaseWrapper

class ChaquopyDatabaseWrapper : PythonDatabaseWrapper {

    private val commands = Python.getInstance()
        .getModule("commands")
        .callAttr("Commands")

    override fun getShoppingListsJson() = commands.callAttr("get_shoppinglists").toString()

    override fun getShoppingListJsonById(id: Int) = commands.callAttr("get_shoppinglist_by_id").toString()

    override fun cleanup() {
        commands.callAttr("cleanup")
    }
}
