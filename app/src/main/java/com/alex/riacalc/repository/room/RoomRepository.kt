package com.alex.riacalc.repository.room

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event
import com.alex.riacalc.repository.BaseRepository

class RoomRepository(val eventDao: RoomDao) : BaseRepository {

    override fun getEventsForDay(selectedDay: String): LiveData<List<Event>> {
        return eventDao.getEventsForDay(selectedDay)
    }

    override fun getEventsForMonth(selectedMonth: String): LiveData<List<Event>> {
        return eventDao.getEventsForMonth(selectedMonth)
    }

    override suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    override suspend fun editEvent(event: Event) {
        eventDao.editEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }
}