package com.alex.riacalc.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.riacalc.model.Event
import java.util.Calendar

class LocalDB(): RepositoryInterface {



    private val list = mutableListOf<Event>()
    private var mutableLiveData = MutableLiveData<List<Event>>()
    val listLiveData: LiveData<List<Event>>
        get() = mutableLiveData

    init {

        list.add(Event(1, 0,10,"", Calendar.getInstance().toString()))
        list.add(Event(2,1,20,"", Calendar.getInstance().toString()))
        list.add(Event(3,2,30,"Таксі на тяжилів", Calendar.getInstance().toString()))
        mutableLiveData.value = list
    }
    override fun getEvent(): LiveData<List<Event>> {
        return listLiveData
    }

    override suspend fun insertEvent(event: Event) {
        list.add(event)
        mutableLiveData.value = list
    }

    override suspend fun deleteEvent(event: Event) {
        list.remove(event)
        mutableLiveData.value = list
    }
}