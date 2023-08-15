package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event

interface EventRepository {

    suspend fun getAllEvents(): LiveData<List<Event>>

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)
}