package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alex.riacalc.model.Event

@Dao
interface MyDao {

    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}