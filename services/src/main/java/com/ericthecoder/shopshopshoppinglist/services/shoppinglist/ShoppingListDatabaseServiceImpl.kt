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
        return try {
            val json = pythonDatabaseWrapper.getShoppingListsJson()
            val listType = Types.newParameterizedType(List::class.java, ShoppingListResponse::class.java)
            val adapter: JsonAdapter<List<ShoppingListResponse>> = moshi.adapter(listType)
            adapter.getOrNull(json) ?: emptyList()
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun getShoppingListById(id: Int): ShoppingListResponse {
        return try {
            val json = pythonDatabaseWrapper.getShoppingListJsonById(id)
            val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
            requireNotNull(adapter.getOrNull(json)) { "Shopping list with id $id not found" }
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun createShoppingList(name: String): ShoppingListResponse {
        return try {
            val json = pythonDatabaseWrapper.insertShoppingList(name.escapeSpecialCharacters())
            val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
            requireNotNull(adapter.getOrNull(json)) { "Shopping list failed to add" }
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun createShopItem(listId: Int, name: String): ShopItemResponse {
        return try {
            val json = pythonDatabaseWrapper.insertShopItem(listId, name.escapeSpecialCharacters(), 1, false)
            val adapter: JsonAdapter<ShopItemResponse> = moshi.adapter(ShopItemResponse::class.java)
            requireNotNull(adapter.getOrNull(json)) { "Shop item failed to add" }
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun updateShoppingList(id: Int, name: String): ShoppingListResponse {
        return try {
            val json = pythonDatabaseWrapper.updateShoppingList(id, name.escapeSpecialCharacters())
            val adapter: JsonAdapter<ShoppingListResponse> = moshi.adapter(ShoppingListResponse::class.java)
            requireNotNull(adapter.getOrNull(json)) { "Shopping list failed to update" }
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun updateShopItem(id: Int, name: String, quantity: Int, checked: Boolean): ShopItemResponse {
        return try {
            val json = pythonDatabaseWrapper.updateShopItem(id, name.escapeSpecialCharacters(), quantity, checked)
            val adapter: JsonAdapter<ShopItemResponse> = moshi.adapter(ShopItemResponse::class.java)
            requireNotNull(adapter.getOrNull(json)) { "Shop item failed to update" }
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun deleteShoppingList(id: Int) {
        try {
            pythonDatabaseWrapper.deleteShoppingList(id)
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    override suspend fun deleteShopItem(id: Int) {
        try {
            pythonDatabaseWrapper.deleteShopItem(id)
        } catch (exception: Exception) {
            throw DbQueryFailedException(exception)
        }
    }

    private fun<T: Any> JsonAdapter<T>.getOrNull(json: String): T? = runCatching {
        fromJson(json)
    }.getOrNull()

    private fun String.escapeSpecialCharacters() = replace("'", "''")
}
