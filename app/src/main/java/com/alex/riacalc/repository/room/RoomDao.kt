package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alex.riacalc.model.EventForDB

@Dao
interface RoomDao {
    @Query("SELECT * FROM events WHERE DATE(date) = :selectedDate")
    fun getEventsForDay(selectedDate: String): LiveData<List<EventForDB>>

    @Query("SELECT * FROM events WHERE strftime('%Y-%m', date) = :selectedMonth")
    fun getEventsForMonth(selectedMonth: String): LiveData<List<EventForDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventForDB)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editEvent(event: EventForDB)

    @Delete
    suspend fun deleteEvent(event: EventForDB)
}