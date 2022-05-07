package com.ericthecoder.dependencies.android.room.service

import com.ericthecoder.dependencies.android.room.dao.ShoppingListDao
import com.ericthecoder.dependencies.android.room.service.mapper.RoomShoppingListDatabaseMapper
import com.ericthecoder.dependencies.android.room.testbuilders.ShoppingListEntityBuilder.aShoppingListEntity
import com.ericthecoder.shopshopshoppinglist.testdata.testdatabuilders.ShoppingListBuilder.aShoppingList
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RoomShoppingListDatabaseServiceTest {

    private val dao: ShoppingListDao = mockk()
    private val mapper: RoomShoppingListDatabaseMapper = mockk()
    private val databaseService = RoomShoppingListDatabaseService(dao, mapper)

    @Test
    fun `test getShoppingLists`() = runTest {
        coEvery { dao.getAll() } returns listOf(aShoppingListEntity())
        coEvery { mapper.mapEntityToShoppingList(any()) } returns aShoppingList()

        val result = databaseService.getShoppingLists()

        assertThat(result).isEqualTo(listOf(aShoppingList()))
    }

    @Test
    fun `test getShoppingListById`() = runTest {
        coEvery { dao.getById(any()) } returns aShoppingListEntity()
        coEvery { mapper.mapEntityToShoppingList(any()) } returns aShoppingList()

        val result = databaseService.getShoppingListById(id = 1)

        assertThat(result).isEqualTo(aShoppingList())
    }

    @Test
    fun `test createShoppingList`() = runTest {
        val id = 1L
        coEvery { dao.insert(any()) } returns id

        val resultId = databaseService.createShoppingList("name")

        assertThat(resultId).isEqualTo(id)
    }

    @Test
    fun `test updateShoppingList`() = runTest {
        coJustRun { dao.update(aShoppingListEntity()) }
        coEvery { mapper.mapShoppingListToEntity(aShoppingList()) } returns aShoppingListEntity()

        databaseService.updateShoppingList(aShoppingList())

        coVerify { dao.update(aShoppingListEntity()) }
    }

    @Test
    fun `test deleteShoppingList`() = runTest {
        val id = 1
        coJustRun { dao.deleteById(id) }

        databaseService.deleteShoppingList(id)

        coVerify { dao.deleteById(id) }
    }
}