package com.ericdecanini.shopshopshoppinglist.util

import kotlin.coroutines.CoroutineContext

@Suppress("PropertyName")
interface CoroutineContextProvider {
    val Main: CoroutineContext
    val IO: CoroutineContext
}
