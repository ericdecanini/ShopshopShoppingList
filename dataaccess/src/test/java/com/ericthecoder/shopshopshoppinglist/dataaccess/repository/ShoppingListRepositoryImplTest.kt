package com.ericthecoder.shopshopshoppinglist.dataaccess.repository

import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import com.ericthecoder.shopshopshoppinglist.usecases.service.ShoppingListDatabaseService
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
class ShoppingListRepositoryImplTest {

    private val databaseService: ShoppingListDatabaseService = mockk()
    private val repository = ShoppingListRepositoryImpl(databaseService)

    @Test
    fun `test getShoppingLists`() = runBlockingTest {
        coEvery { databaseService.getShoppingLists() } returns listOf(aShoppingList())

        val result = repository.getShoppingLists()

        assertThat(result).isEqualTo(listOf(aShoppingList()))
    }

    @Test
    fun `test getShoppingListById`() = runBlockingTest {
        val id = 1
        coEvery { databaseService.getShoppingListById(id) } returns aShoppingList()

        val result = repository.getShoppingListById(id)

        assertThat(result).isEqualTo(aShoppingList())
    }

    @Test
    fun `test createNewShoppingList`() = runBlockingTest {
        val id = 1
        coEvery { databaseService.createShoppingList(any()) } returns id
        coEvery { databaseService.getShoppingListById(id) } returns aShoppingList()

        val result = repository.createNewShoppingList("name")

        assertThat(result).isEqualTo(aShoppingList())
    }

    @Test
    fun `test updateShoppingList`() = runBlockingTest {
        coJustRun { databaseService.updateShoppingList(any()) }

        repository.updateShoppingList(aShoppingList())

        coVerify { databaseService.updateShoppingList(aShoppingList()) }
    }

    @Test
    fun `test deleteShoppingList`() = runBlockingTest {
        val id = 1
        coJustRun { databaseService.deleteShoppingList(any()) }

        repository.deleteShoppingList(id)

        coVerify { databaseService.deleteShoppingList(id) }
    }
}