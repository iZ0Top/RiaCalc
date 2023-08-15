package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event

class RoomEventRepository(val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents(): LiveData<List<Event>> {
        return eventDao.getAllEvents()
    }

    override suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }
}