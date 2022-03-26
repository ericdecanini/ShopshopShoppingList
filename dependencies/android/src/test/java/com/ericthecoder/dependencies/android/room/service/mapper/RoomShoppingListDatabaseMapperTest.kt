package com.ericthecoder.dependencies.android.room.service.mapper

import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.dependencies.android.room.testbuilders.ShoppingListEntityBuilder.aShoppingListEntity
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoomShoppingListDatabaseMapperTest {

    private val mapper = RoomShoppingListDatabaseMapper()

    @Test
    fun `test map shopping list to entity`() {
        val shoppingList = aShoppingList()

        val result = mapper.mapShoppingListToEntity(shoppingList)

        assertThat(result).isEqualTo(ShoppingListEntity(
            shoppingList.id,
            shoppingList.name,
            shoppingList.items
        ))
    }

    @Test
    fun `test map entity to shopping list`() {
        val entity = aShoppingListEntity()

        val result = mapper.mapEntityToShoppingList(entity)

        assertThat(result).isEqualTo(ShoppingList(
            entity.id,
            entity.name,
            entity.items.toMutableList()
        ))
    }
}