package com.alex.riacalc.repository.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.alex.riacalc.model.EventForDB
import com.alex.riacalc.repository.BaseRepository

class RoomRepository(val eventDao: RoomDao) : BaseRepository {

    override fun getEventsForDay(selectedDay: String): LiveData<List<EventForDB>> {
        return eventDao.getEventsForDay(selectedDay)
    }

    override fun getEventsForMonth(selectedMonth: String): LiveData<List<EventForDB>> {
        return eventDao.getEventsForMonth(selectedMonth)
    }

    override suspend fun insertEvent(event: EventForDB) {
        eventDao.insertEvent(event)
    }

    override suspend fun editEvent(event: EventForDB) {
        eventDao.editEvent(event)
    }

    override suspend fun deleteEvent(event: EventForDB) {
        eventDao.deleteEvent(event)
    }
}