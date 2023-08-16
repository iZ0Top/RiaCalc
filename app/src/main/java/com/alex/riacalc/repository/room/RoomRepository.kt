package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event
import com.alex.riacalc.repository.BaseRepository

class RoomRepository(val eventDao: RoomDao) : BaseRepository {

    override val allEvents: LiveData<List<Event>>
        get() = eventDao.getAllEvents()

    override suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }

//    override suspend fun updateEvent(event: Event) {
//        eventDao.editEvent(event)
//    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }
}