package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.testdata.testdatabuilders.ShopItemBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = ListViewModel()

    @Test
    fun givenNewItemName_whenOnAddItemClick_thenItemAddedAndSet() {
        val itemName = "name"

        viewModel.onAddItemClick(itemName)

        val list = viewModel.listLiveData.value
        val expectedList = listOf(ShopItemBuilder.aShopItem().withName(itemName).build())
        assertThat(list).isEqualTo(expectedList)
    }

    @Test
    fun givenNewItemNames_whenOnMultipleAddItemClick_thenItemAddedAndSet() {
        val itemName1 = "name1"
        val itemName2 = "name2"

        viewModel.onAddItemClick(itemName1)
        viewModel.onAddItemClick(itemName2)

        val listData = viewModel.listLiveData.value
        val expectedList = listOf(
            ShopItemBuilder.aShopItem().withName(itemName1).build(),
            ShopItemBuilder.aShopItem().withName(itemName2).build()
        )
        assertThat(listData).isEqualTo(expectedList)
    }

    @Test
    fun givenListWithShopItem_whenOnItemNameChanged_thenNewItemSet() {
        viewModel.onAddItemClick("name1")
        viewModel.onAddItemClick("name2")
        val list = viewModel.listLiveData.value

        val newName = "newName2"
        val expectedNewShopItem = list!![1].copy(name = newName)
        viewModel.onItemNameChanged(list[1], newName)

        assertThat(list[1]).isEqualTo(expectedNewShopItem)
    }
}
