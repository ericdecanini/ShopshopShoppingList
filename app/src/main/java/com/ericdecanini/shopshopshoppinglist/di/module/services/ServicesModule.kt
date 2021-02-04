package com.ericdecanini.shopshopshoppinglist.di.module.services

import com.ericdecanini.shopshopshoppinglist.di.module.services.shoppinglist.ShoppingListModule
import dagger.Module

@Module(includes = [
    ShoppingListModule::class
])
class ServicesModule
