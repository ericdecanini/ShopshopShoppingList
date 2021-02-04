package com.ericdecanini.shopshopshoppinglist.usecases.repository

interface ShoppingListRepository {

    fun getShoppingLists()

    fun getShoppingListById(id: Int)

}
