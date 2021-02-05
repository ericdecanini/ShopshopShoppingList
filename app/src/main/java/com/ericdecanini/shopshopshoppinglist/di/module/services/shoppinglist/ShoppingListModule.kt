package com.ericdecanini.shopshopshoppinglist.di.module.services.shoppinglist

import com.ericdecanini.shopshopshoppinglist.dataaccess.repository.SQLiteShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.services.shoppinglist.ShoppingListDatabaseServiceImpl
import com.ericdecanini.shopshopshoppinglist.usecases.database.PythonDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericdecanini.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import dagger.Module
import dagger.Provides

@Module
class ShoppingListModule {

    @Provides
    fun provideShoppingListDatabaseService(
        pythonDatabaseWrapper: PythonDatabaseWrapper
    ): ShoppingListDatabaseService = ShoppingListDatabaseServiceImpl(pythonDatabaseWrapper)

    @Provides
    fun provideShoppingListRepository(
        shoppingListDatabaseService: ShoppingListDatabaseService
    ): ShoppingListRepository = SQLiteShoppingListRepository(shoppingListDatabaseService)

}
