package com.ericdecanini.shopshopshoppinglist.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CoroutineContextProviderImpl : CoroutineContextProvider {

    override val Main: CoroutineContext by lazy { Dispatchers.Main }
    override val IO: CoroutineContext by lazy { Dispatchers.IO }
}
