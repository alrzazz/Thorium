package com.example.thorium.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: Status): Long

    @Query("SELECT * FROM status ORDER BY timestamp ASC;")
    fun getAll(): Flow<List<Status>>

}