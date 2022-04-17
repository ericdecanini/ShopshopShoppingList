package com.ericthecoder.shopshopshoppinglist.di.module.dataaccess

import com.ericthecoder.shopshopshoppinglist.dataaccess.repository.ShoppingListRepositoryImpl
import com.ericthecoder.shopshopshoppinglist.usecases.repository.ShoppingListRepository
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import dagger.Module
import dagger.Provides

@Module
class DataAccessModule {

    @Provides
    fun provideShoppingListRepository(
        shoppingListDatabaseService: ShoppingListDatabaseService,
    ): ShoppingListRepository = ShoppingListRepositoryImpl(
        shoppingListDatabaseService,
    )
}