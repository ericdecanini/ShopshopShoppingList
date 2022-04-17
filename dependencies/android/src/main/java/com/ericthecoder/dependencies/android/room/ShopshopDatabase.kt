package com.ericthecoder.dependencies.android.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ericthecoder.dependencies.android.room.dao.ShoppingListDao
import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.dependencies.android.room.typeconverter.ShopItemListConverter

@Database(
    entities = [ShoppingListEntity::class],
    version = 1,
)
@TypeConverters(ShopItemListConverter::class)
abstract class ShopshopDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}