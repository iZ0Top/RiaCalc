package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alex.riacalc.model.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): LiveData<List<Event>>

    @Insert
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun editEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}