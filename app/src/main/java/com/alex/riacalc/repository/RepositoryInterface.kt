package com.alex.riacalc.repository

import androidx.lifecycle.LiveData
import com.alex.riacalc.model.Event

interface RepositoryInterface {

    fun getEvent(): LiveData<List<Event>>

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)
}