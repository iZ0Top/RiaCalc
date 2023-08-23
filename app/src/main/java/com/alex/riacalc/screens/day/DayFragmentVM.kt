package com.alex.riacalc.screens.day

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alex.riacalc.model.Event
import com.alex.riacalc.repository.room.AppDatabase
import com.alex.riacalc.repository.room.RoomRepository
import com.alex.riacalc.utils.REPOSITORY
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DayFragmentVM(application: Application) : AndroidViewModel(application) {

    private val context = application

    private val _eventListLD = MutableLiveData<List<Event>>()
    private var _calendarLD = MutableLiveData<Calendar>()
    private var _statisticLD = MutableLiveData<Statistic>()

    val eventListLD: LiveData<List<Event>> get() = _eventListLD
    val calendarLD: LiveData<Calendar> get() = _calendarLD
    val statisticLD: LiveData<Statistic> get() = _statisticLD


    private val mediatorLiveData = MediatorLiveData<List<Event>>()

    init {
        Log.d("TAG", "DayFragmentVM - init")
        _calendarLD.value = Calendar.getInstance()
        initDatabase()
        mediatorLiveData.addSource(REPOSITORY.eventDao.getAllEvents()){
            mediatorLiveData.value = it
        }
    }

    private fun initDatabase() {
        Log.d("TAG", "DayFragmentVM - initDatabase")
        val dao = AppDatabase.getInstance(context).getEventDao()
        REPOSITORY = RoomRepository(dao)
    }

     fun loadEventsForDate(calendar: Calendar): LiveData<List<Event>> {
         Log.d("TAG", "DayFragmentVM - loadEvents")

         mediatorLiveData.apply {
             removeSource(mediatorLiveData)}


         val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
         val date = dateFormatter.format(calendar.time)
         Log.d("TAG", "DayFragmentVM - loadEvents: date=$date")
//         _eventListLD = REPOSITORY.eventDao.getAllEvents()
//         Log.d("TAG", "DayFragmentVM - loadEvents: date=${_eventListLD.toString()}")
         return REPOSITORY.eventDao.getAllEvents()
    }

    fun insertEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.eventDao.insertEvent(event)
        }
    }

    fun editEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.editEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.eventDao.deleteEvent(event)
        }
    }

    fun setNewDate(calendar: Calendar) {
        _calendarLD.value = calendar
    }

    fun calculateDay(list: List<Event>) {

        var inspectionsCount = 0
        var inspectionsSum = 0
        var tripsCount = 0
        var tripsSum = 0
        var otherCount = 0
        var otherSum = 0

        for (i in list) {
            when (i.type){
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
            inspectionsCount = inspectionsCount.toString(),
            inspectionsSum = inspectionsSum.toString(),
            tripsCount = tripsCount.toString(),
            tripsSum = tripsSum.toString(),
            otherCount = otherCount.toString(),
            otherSum = otherSum.toString()
        )
    }




    override fun onCleared() {
        super.onCleared()

    }

    companion object {
        data class Statistic(
            var inspectionsCount: String,
            var inspectionsSum: String,
            var tripsCount: String,
            var tripsSum: String,
            var otherCount: String,
            var otherSum: String
        )
    }
}

