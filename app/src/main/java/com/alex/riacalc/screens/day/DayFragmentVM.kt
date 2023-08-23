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

    private var _calendarLD = MutableLiveData<Calendar>()
    private var _statisticLD = MutableLiveData<Statistic>()

    val calendarLD: LiveData<Calendar> get() = _calendarLD
    val statisticLD: LiveData<Statistic> get() = _statisticLD


    private val mediatorLiveData = MediatorLiveData<List<Event>>()

    init {
        Log.d("TAG", "DayFragmentVM - init")
        val currentDate = Calendar.getInstance()
        _calendarLD.value = currentDate

        initDatabase()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val d = dateFormatter.format(currentDate.time)

        mediatorLiveData.addSource(REPOSITORY.eventDao.getAllEvents(d)) {
            mediatorLiveData.value = it
        }
    }

    private fun initDatabase() {
        Log.d("TAG", "DayFragmentVM - initDatabase")
        val dao = AppDatabase.getInstance(context).getEventDao()
        REPOSITORY = RoomRepository(dao)
    }

    fun getMediatorLiveData(): LiveData<List<Event>>{
        return mediatorLiveData
    }
    fun loadEventsForDate(calendar: Calendar){
        Log.d("TAG", "DayFragmentVM - loadEvents")

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDate = dateFormatter.format(calendar.time)

        mediatorLiveData.removeSource(mediatorLiveData)
        mediatorLiveData.addSource(REPOSITORY.eventDao.getAllEvents(selectedDate)){
            mediatorLiveData.value = it
        }

        Log.d("TAG", "DayFragmentVM - loadEvents: date=$selectedDate")
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

