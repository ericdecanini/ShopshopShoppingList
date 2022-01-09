package com.ericthecoder.shopshopshoppinglist.services.shoppinglist

import com.ericthecoder.shopshopshoppinglist.entities.database.DbQueryFailedException
import com.ericthecoder.shopshopshoppinglist.entities.network.ShopItemResponse
import com.ericthecoder.shopshopshoppinglist.entities.network.ShoppingListResponse
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
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

    override suspend fun getShoppingLists(): List<ShoppingListResponse> {
        val json = pythonDatabaseWrapper.getShoppingListsJson()
        val listType = Types.newParameterizedType(List::class.java, ShoppingListResponse::class.java)
        val adapter: JsonAdapter<List<ShoppingListResponse>> = moshi.adapter(listType)
        return adapter.getOrNull(json) ?: emptyList()
    }

    override suspend fun getShoppingListById(id: Int): ShoppingListResponse {
        val json = pythonDatabaseWrapper.getShoppingListJsonById(id)
        val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
        return adapter.getOrNull(json) ?: throw DbQueryFailedException("Shop item with id $id not found")
    }

    override suspend fun createShoppingList(name: String): ShoppingListResponse {
        val json = pythonDatabaseWrapper.insertShoppingList(name.escapeSpecialCharacters())
        val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
        return adapter.getOrNull(json) ?: throw DbQueryFailedException("Shopping list failed to add")
    }

    override suspend fun createShopItem(listId: Int, name: String): ShopItemResponse {
        val json = pythonDatabaseWrapper.insertShopItem(listId, name.escapeSpecialCharacters(), 1, false)
        val adapter: JsonAdapter<ShopItemResponse> = moshi.adapter(ShopItemResponse::class.java)
        return adapter.getOrNull(json) ?: throw DbQueryFailedException("Shop item failed to add")
    }

    override suspend fun updateShoppingList(id: Int, name: String): ShoppingListResponse {
        val json = pythonDatabaseWrapper.updateShoppingList(id, name.escapeSpecialCharacters())
        val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
        return adapter.getOrNull(json) ?: throw DbQueryFailedException("Shopping list failed to update")
    }

    override suspend fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): ShopItemResponse {
        val json = pythonDatabaseWrapper.updateShopItem(id, name.escapeSpecialCharacters(), quantity, checked)
        val adapter: JsonAdapter<ShopItemResponse> = moshi.adapter(ShopItemResponse::class.java)
        return adapter.getOrNull(json) ?: throw DbQueryFailedException("Shop item failed to update")
    }

    override suspend fun deleteShoppingList(id: Int) {
        pythonDatabaseWrapper.deleteShoppingList(id)
    }

    override suspend fun deleteShopItem(id: Int) {
        pythonDatabaseWrapper.deleteShopItem(id)
    }

    private fun<T: Any> JsonAdapter<T>.getOrNull(json: String): T? = runCatching {
        fromJson(json)
    }.getOrNull()

    private fun String.escapeSpecialCharacters() = replace("'", "''")
}
