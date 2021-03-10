package com.ericdecanini.shopshopshoppinglist.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestCoroutineContextProvider : CoroutineContextProvider {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    override val Main = testCoroutineDispatcher
    override val IO = testCoroutineDispatcher
}
