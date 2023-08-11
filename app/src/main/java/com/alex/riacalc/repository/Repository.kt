package com.alex.riacalc.repository

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event

class Repository: RepositoryInterface {

    private val db = LocalDB()
    override fun getEvent(): LiveData<List<Event>> {
        return db.getEvent()
    }

    override suspend fun insertEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(event: Event) {
        TODO("Not yet implemented")
    }
}