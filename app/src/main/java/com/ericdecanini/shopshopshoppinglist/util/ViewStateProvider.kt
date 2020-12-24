package com.ericdecanini.shopshopshoppinglist.util

import com.ericdecanini.entities.ViewState

class ViewStateProvider {

    fun <T: ViewState> create(entityClass: Class<T>): T = entityClass.newInstance()
}
