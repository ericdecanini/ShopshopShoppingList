package com.ericthecoder.shopshopshoppinglist.dependencies.chaquopy

import com.chaquo.python.Python
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper

class ChaquopyDatabaseWrapper : PythonDatabaseWrapper {

    private val commands by lazy {
        Python.getInstance()
            .getModule("commands")
            .callAttr("Commands")
    }

    override suspend fun getShoppingListsJson()
        = commands.callAttr("get_shoppinglists").toString()

    override suspend fun getShoppingListJsonById(id: Int)
        = commands.callAttr("get_shoppinglist_by_id", id).toString()

    override suspend fun insertShoppingList(name: String)
        = commands.callAttr("insert_shoppinglist", name).toString()

    override suspend fun insertShopItem(listId: Int, name: String, quantity: Int, checked: Boolean)
        = commands.callAttr("insert_shopitem", listId, name, quantity, checked).toString()

    override suspend fun updateShoppingList(id: Int, name: String)
        = commands.callAttr("update_shoppinglist", id, name).toString()

    override suspend fun updateShopItem(currentName: String, newName: String, quantity: Int, checked: Boolean)
        = commands.callAttr("update_shopitem", currentName, newName, quantity, checked).toString()

    override suspend fun deleteShoppingList(id: Int) {
        commands.callAttr("delete_shoppinglist", id)
    }

    override suspend fun deleteShopItem(name: String) {
        commands.callAttr("delete_shopitem", name)
    }

    override suspend fun cleanup() {
        commands.callAttr("cleanup")
    }
}

