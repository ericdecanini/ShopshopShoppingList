package com.ericdecanini.shopshopshoppinglist.services.shoppinglist

import com.ericdecanini.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericdecanini.shopshopshoppinglist.entities.network.ShoppingListResponse
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ShoppingListDatabaseServiceImpl(
    private val pythonDatabaseWrapper: PythonDatabaseWrapper
) : ShoppingListDatabaseService {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun getShoppingLists(): List<ShoppingListResponse>? {
        val json = pythonDatabaseWrapper.getShoppingListsJson()
        val listType = Types.newParameterizedType(List::class.java, ShoppingListResponse::class.java)
        val adapter: JsonAdapter<List<ShoppingListResponse>> = moshi.adapter(listType)
        return adapter.fromJson(json)
    }

    override fun getShoppingListById(id: Int): ShoppingListResponse? {
        val json = pythonDatabaseWrapper.getShoppingListJsonById(id)
        val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
        return adapter.fromJson(json)
    }

    override fun createShoppingList(name: String): ShoppingListResponse {
        val json = pythonDatabaseWrapper.insertShoppingList(name)
        val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
        return adapter.fromJson(json) ?: throw IllegalStateException("Shopping List failed to add")
    }

    override fun createShopItem(listId: Int, name: String): ShopItemResponse {
        val json = pythonDatabaseWrapper.insertShopItem(listId, name, 1, false)
        val adapter: JsonAdapter<ShopItemResponse> = moshi.adapter(ShopItemResponse::class.java)
        return adapter.fromJson(json) ?: throw IllegalStateException("ShopItem failed to add")
    }

    override fun updateShoppingList(id: Int, name: String): ShoppingListResponse? {
        val json = pythonDatabaseWrapper.updateShoppingList(id, name)
        val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
        return adapter.fromJson(json)
    }

    override fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): ShopItemResponse? {
        val json = pythonDatabaseWrapper.updateShopItem(id, name, quantity, checked)
        val adapter: JsonAdapter<ShopItemResponse> = moshi.adapter(ShopItemResponse::class.java)
        return adapter.fromJson(json)
    }

    override fun deleteShoppingList(id: Int) {
        pythonDatabaseWrapper.deleteShoppingList(id)
    }

    override fun deleteShopItem(id: Int) {
        pythonDatabaseWrapper.deleteShopItem(id)
    }
}
