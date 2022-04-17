package com.ericthecoder.dependencies.android.room.typeconverter

import androidx.room.TypeConverter
import com.ericthecoder.shopshopshoppinglist.entities.ShopItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ShopItemListConverter {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private var shopItemListType = Types.newParameterizedType(List::class.java, ShopItem::class.java)
    private val jsonAdapter = moshi.adapter<List<ShopItem>>(shopItemListType)

    @TypeConverter
    fun convertShopItemsToJson(shopItems: List<ShopItem>): String = jsonAdapter.toJson(shopItems)

    @TypeConverter
    fun convertJsonShopItems(json: String) = jsonAdapter.fromJson(json)
}