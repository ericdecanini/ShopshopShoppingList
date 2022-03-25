package com.ericthecoder.dependencies.android.room.dao

import androidx.room.*
import com.ericthecoder.dependencies.android.room.entity.ShoppingListEntity

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists")
    suspend fun getAll(): List<ShoppingListEntity>

    @Insert
    suspend fun insert(entity: ShoppingListEntity)

    @Update
    suspend fun update(entity: ShoppingListEntity)

    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteById(id: Int)
}