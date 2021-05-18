package com.ericthecoder.shopshopshoppinglist.util.providers

import kotlin.coroutines.CoroutineContext

@Suppress("PropertyName")
interface CoroutineContextProvider {
    val Main: CoroutineContext
    val IO: CoroutineContext
}
