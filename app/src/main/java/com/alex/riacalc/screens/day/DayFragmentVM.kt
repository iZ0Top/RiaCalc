package com.alex.riacalc.screens.day

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alex.riacalc.model.Day
import com.alex.riacalc.model.Event
import com.alex.riacalc.model.EventForDB
import com.alex.riacalc.repository.room.AppDatabase
import com.alex.riacalc.repository.room.RoomRepository
import com.alex.riacalc.utils.REPOSITORY
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import com.alex.riacalc.utils.convertDateAndTimeToString
import com.alex.riacalc.utils.convertDateToString
import com.alex.riacalc.utils.toEvent
import com.alex.riacalc.utils.toEventForDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class DayFragmentVM(application: Application) : AndroidViewModel(application) {

    private val context = application

    private var currentListLD: LiveData<List<EventForDB>>? = null

    private var mediatorLiveData = MediatorLiveData<List<Event>>()
    private var _calendarLD = MutableLiveData<Calendar>()
    private var _statisticLD = MutableLiveData<Statistic>()


    val calendarLD: LiveData<Calendar> get() = _calendarLD
    val statisticLD: LiveData<Statistic> get() = _statisticLD

    init {
        Log.d("DAY", "DayFragmentVM - init")
        initDatabase()
        val currentDate = Calendar.getInstance()
        _calendarLD.value = currentDate

    }

    private fun initDatabase() {
        Log.d("DAY", "DayFragmentVM - initDatabase")
        val dao = AppDatabase.getInstance(context).getEventDao()
        REPOSITORY = RoomRepository(dao)
    }

    fun getMediatorLiveData(): LiveData<List<Event>>{
        Log.d("DAY", "DayFragmentVM - getMediatorLiveData")
        return mediatorLiveData
    }

    fun loadEventsForDay(calendar: Calendar){
        Log.d("DAY", "DayFragmentVM - loadEventsForDay")
        val day = convertDateToString(calendar)
        Log.d("DAY", "DayFragmentVM - loadEventsForDay, day = $day")

        currentListLD?.let {
            mediatorLiveData.removeSource(it)
        }

        val eventsLD = REPOSITORY.eventDao.getEventsForDay(day)

        currentListLD = eventsLD

        mediatorLiveData.addSource(eventsLD){ listEventForDB ->
            Log.d("DAY", "DayFragmentVM - loadEventsForDay. Loaded list size: ${listEventForDB.size}")
            val listEvent = listEventForDB.map { toEvent(it) }.toList()
            Log.d("DAY", "DayFragmentVM - loadEventsForDay. Converted list size: ${listEvent.size}")
            mediatorLiveData.value = listEvent
        }

    }

    fun insertEvent(event: Event) {
        val eventForDB = toEventForDB(event)
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.eventDao.insertEvent(eventForDB)
        }
    }

    fun editEvent(event: Event) {
        val eventForDB = toEventForDB(event)
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.eventDao.editEvent(eventForDB)
        }
    }

    fun deleteEvent(event: Event) {
        val eventForDB = toEventForDB(event)
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.eventDao.deleteEvent(eventForDB)
        }
    }

    fun setNewDate(calendar: Calendar) {
        Log.d("DAY", "DayFragmentVM - setNewDate")
        _calendarLD.value = calendar
    }

    fun calculateDay(list: List<Event>) {
        Log.d("DAY", "DayFragmentVM - calculateDay")
        var inspectionsCount = 0
        var inspectionsSum = 0
        var tripsCount = 0
        var tripsSum = 0
        var otherCount = 0
        var otherSum = 0

        for (i in list) {
            when (i.type) {
                TYPE_INSPECTION -> {
                    inspectionsCount++
                    inspectionsSum += i.cost
                }
                TYPE_TRIP -> {
                    tripsCount++
                    tripsSum += i.cost
                }
                TYPE_OTHER -> {
                    otherCount++
                    otherSum += i.cost
                }
            }
        }
        _statisticLD.value = Statistic(
            inspectionsCount = inspectionsCount,
            inspectionsSum = inspectionsSum,
            tripsCount = tripsCount,
            tripsSum = tripsSum,
            otherCount = otherCount,
            otherSum = otherSum
        )
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        data class Statistic(
            var inspectionsCount: Int,
            var inspectionsSum: Int,
            var tripsCount: Int,
            var tripsSum: Int,
            var otherCount: Int,
            var otherSum: Int
        )
    }
}

