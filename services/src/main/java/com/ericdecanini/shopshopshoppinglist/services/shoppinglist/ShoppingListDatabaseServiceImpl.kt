package com.ericdecanini.shopshopshoppinglist.services.shoppinglist

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
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

    override fun getShoppingLists(): List<ShoppingList>? {
        val json = pythonDatabaseWrapper.getShoppingListsJson()
        val listType = Types.newParameterizedType(List::class.java, ShoppingList::class.java)
        val adapter: JsonAdapter<List<ShoppingList>> = moshi.adapter(listType)
        return adapter.fromJson(json)
    }

    override fun getShoppingListById(id: Int): ShoppingList {
        TODO("Not yet implemented")
    }
}
