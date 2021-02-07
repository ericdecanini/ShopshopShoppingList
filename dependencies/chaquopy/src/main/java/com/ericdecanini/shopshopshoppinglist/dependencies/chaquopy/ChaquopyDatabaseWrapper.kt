package com.ericdecanini.shopshopshoppinglist.dependencies.chaquopy

import com.chaquo.python.Python
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper

class ChaquopyDatabaseWrapper : PythonDatabaseWrapper {

    private val commands by lazy {
        Python.getInstance()
            .getModule("commands")
            .callAttr("Commands")
    }

    override fun getShoppingListsJson()
        = commands.callAttr("get_shoppinglists").toString()

    override fun getShoppingListJsonById(id: Int)
        = commands.callAttr("get_shoppinglist_by_id", id).toString()

    override fun insertShoppingList(name: String)
        = commands.callAttr("insert_shoppinglist", name).toString()

    override fun insertShopItem(listId: Int, name: String, quantity: Int, checked: Boolean)
        = commands.callAttr("insert_shopitem", listId, name, quantity, checked).toString()

    override fun updateShoppingList(id: Int, name: String)
        = commands.callAttr("update_shoppinglist", id, name).toString()

    override fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean)
        = commands.callAttr("update_shopitem", id, name, quantity, checked).toString()

    override fun deleteShoppingList(id: Int)
        = commands.callAttr("delete_shoppinglist", id).toString()

    override fun deleteShopItem(id: Int)
        = commands.callAttr("delete_shopitem", id).toString()

    override fun cleanup() {
        commands.callAttr("cleanup")
    }

    private fun insertSomeItems() {
        commands.callAttr("insert_shoppinglist", "Sample List")
        commands.callAttr("insert_shoppinglist", "Sample List")
        commands.callAttr("insert_shoppinglist", "Sample List")
    }
}

