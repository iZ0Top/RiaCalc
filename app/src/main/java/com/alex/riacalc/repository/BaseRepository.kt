package com.alex.riacalc.repository

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event

interface BaseRepository {

    fun getEventsForDay(selectedDay: String): LiveData<List<Event>>

    fun getEventsForMonth(selectedMonth: String): LiveData<List<Event>>

    suspend fun insertEvent(event: Event)

    suspend fun editEvent(event: Event)

    suspend fun deleteEvent(event: Event)

}