package com.ericdecanini.shopshopshoppinglist.entities

data class ShoppingList(
    val id: Int,
    val title: String,
    val items: MutableList<ShopItem>
) {

    companion object {
        fun generateDummyLists(): List<ShoppingList> = mutableListOf(
            ShoppingList(0, "Shoplist Juan", mutableListOf(
                ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
            )),
            ShoppingList(1, "Shoplist Toooh", mutableListOf(
                ShopItem.newItem("Limen"), ShopItem.newItem("Flower")
            )),
            ShoppingList(2, "Shoplist Tree", mutableListOf(
                ShopItem.newItem("Cofveve")
            ))
        )

        fun generateDummyList(): ShoppingList = ShoppingList(0, "Shoplist Juan", mutableListOf(
            ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
        ))
    }

}
