package com.ericdecanini.shopshopshoppinglist.entities

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ShopItemTest {

    private val shopItem = ShopItem(DEFAULT_NAME, DEFAULT_QUANTITY, DEFAULT_CHECKED)

    @Test
    fun givenNewName_whenWithName_thenShopItemWithNameReturned() {
        val newName = "new_name"

        val newShopItem = shopItem.withName(newName)

        assertThat(newShopItem).isEqualTo(shopItem.copy(name = newName))
    }

    @Test
    fun givenNewQuantity_whenWithQuantity_thenShopItemWithNewQuantityReturned() {
        val newQuantity = 5

        val newShopItem = shopItem.withQuantity(newQuantity)

        assertThat(newShopItem).isEqualTo(shopItem.copy(quantity = newQuantity))
    }

    @Test
    fun givenNewChecked_whenWithChecked_thenShopItemWithNewCheckedReturned() {
        val newChecked = true

        val newShopItem = shopItem.withChecked(newChecked)

        assertThat(newShopItem).isEqualTo(shopItem.copy(checked = newChecked))
    }

    companion object {
        private const val DEFAULT_NAME = "default_name"
        private const val DEFAULT_QUANTITY = 1
        private const val DEFAULT_CHECKED = false
    }
}
