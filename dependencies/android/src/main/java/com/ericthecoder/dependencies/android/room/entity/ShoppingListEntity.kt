package com.ericthecoder.dependencies.android.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ericthecoder.dependencies.android.room.typeconverter.ShopItemListConverter
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @TypeConverters(ShopItemListConverter::class)
    val items: List<ShopItem>,
)