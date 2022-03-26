package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.room

import com.ericthecoder.dependencies.android.room.ShopshopDatabase
import com.ericthecoder.dependencies.android.room.dao.ShoppingListDao
import com.ericthecoder.dependencies.android.room.service.RoomShoppingListDatabaseService
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Provides
    fun provideShoppingListDao(
        database: ShopshopDatabase
    ): ShoppingListDao = database.shoppingListDao()

    @Provides
    fun provideShoppingListDatabaseService(
        shoppingListDao: ShoppingListDao
    ): ShoppingListDatabaseService = RoomShoppingListDatabaseService(shoppingListDao)
}