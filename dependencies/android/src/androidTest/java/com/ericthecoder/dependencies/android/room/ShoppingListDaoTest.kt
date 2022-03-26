package com.ericthecoder.dependencies.android.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ericthecoder.dependencies.android.room.dao.ShoppingListDao
import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity
import com.ericthecoder.dependencies.android.room.testbuilders.ShoppingListEntityBuilder.aShoppingListEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingListDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: ShoppingListDao
    private lateinit var database: ShopshopDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ShopshopDatabase::class.java).build()
        dao = database.shoppingListDao()
    }

    @Test
    fun testInsertAndGet() = runBlockingTest {
        val entity = aShoppingListEntity()

        val id = dao.insert(entity)
        val resultFromDb = dao.getById(id.toInt())

        resultFromDb.assertEqualComparingContents(entity)
    }

    @Test
    fun testInsertUpdateAndGet() = runBlockingTest {
        val entity = aShoppingListEntity()
        dao.insert(entity)

        val insertedEntity = dao.getAll().first()
        val entityWithChanges = insertedEntity.copy(name = "new_name")
        dao.update(entityWithChanges)

        val resultFromDb = dao.getAll().first()
        resultFromDb.assertEqualComparingContents(entityWithChanges)
    }

    @Test
    fun testInsertDeleteAndGetReturningNothing() = runBlockingTest {
        val entity = aShoppingListEntity()

        val id = dao.insert(entity)
        dao.deleteById(id.toInt())
        val resultFromDb = dao.getAll()

        assertThat(resultFromDb).isEmpty()
    }

    private fun ShoppingListEntity?.assertEqualComparingContents(expected: ShoppingListEntity?) {
        assertThat(this?.name).isEqualTo(expected?.name)
        assertThat(this?.items).isEqualTo(expected?.items)
    }

    @After
    fun teardown() {
        database.close()
    }
}