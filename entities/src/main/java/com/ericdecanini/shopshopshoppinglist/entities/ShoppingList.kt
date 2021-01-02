package com.ericdecanini.shopshopshoppinglist.entities

data class ShoppingList(
    val id: Int,
    val title: String,
    val items: List<ShopItem>
) {

    companion object {
        fun generateDummyLists(): List<ShoppingList> = listOf(
            ShoppingList(0, "Shoplist Juan", listOf(
                ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
            )),
            ShoppingList(1, "Shoplist Toooh", listOf(
                ShopItem.newItem("Limen"), ShopItem.newItem("Flower")
            )),
            ShoppingList(2, "Shoplist Tree", listOf(
                ShopItem.newItem("Cofveve")
            ))
        )

        fun generateDummyList(): ShoppingList = ShoppingList(0, "Shoplist Juan", listOf(
            ShopItem.newItem("Oringe"), ShopItem.newItem("Limen"), ShopItem.newItem("Egg Nudels"), ShopItem.newItem("Rais"), ShopItem.newItem("Whine")
        ))
    }

}
