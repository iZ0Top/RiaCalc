package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alex.riacalc.model.Event

@Dao
interface RoomDao {
    @Query("SELECT * FROM events WHERE DATE(date) = :selectedDate")
    fun getEventsForDay(selectedDate: String): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE strftime('%Y-%m', date) = :selectedMonth")
    fun getEventsForMonth(selectedMonth: String): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}