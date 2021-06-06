package com.ericthecoder.shopshopshoppinglist.di.module.services.shoppinglist

import com.ericthecoder.shopshopshoppinglist.dataaccess.mapper.SQLiteShoppingListMapper
import com.ericthecoder.shopshopshoppinglist.dataaccess.mapper.ShoppingListMapper
import com.ericthecoder.shopshopshoppinglist.dataaccess.repository.SQLiteShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.services.shoppinglist.ShoppingListDatabaseServiceImpl
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
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
        shoppingListDatabaseService: ShoppingListDatabaseService,
        shoppingListMapper: ShoppingListMapper
    ): ShoppingListRepository = SQLiteShoppingListRepository(
        shoppingListDatabaseService,
        shoppingListMapper
    )

    @Provides
    fun provideShoppingListMapper(): ShoppingListMapper = SQLiteShoppingListMapper()

}
