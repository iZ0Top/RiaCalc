package com.alex.riacalc.repository

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.EventForDB

interface BaseRepository {

    fun getEventsForDay(selectedDay: String): LiveData<List<EventForDB>>

    fun getEventsForMonth(selectedMonth: String): LiveData<List<EventForDB>>

    suspend fun insertEvent(event: EventForDB)

    suspend fun editEvent(event: EventForDB)

    suspend fun deleteEvent(event: EventForDB)

}