package com.ericthecoder.shopshopshoppinglist.di.module.services

import com.ericthecoder.shopshopshoppinglist.di.module.services.shoppinglist.ShoppingListModule
import dagger.Module

@Module(includes = [
    ShoppingListModule::class
])
class ServicesModule
