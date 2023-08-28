package com.alex.riacalc.screens.month

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.model.Day
import com.alex.riacalc.model.Event
import com.alex.riacalc.model.EventForDB
import com.alex.riacalc.utils.REPOSITORY
import com.alex.riacalc.utils.toEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthFragmentVM: ViewModel() {


    private val mediatorLiveData = MediatorLiveData<List<Event>>()

    private var _calendarLD = MutableLiveData<Calendar>()
    val calendarLD: LiveData<Calendar> get() = _calendarLD


    init {
        Log.d("TAGM", "MonthFragmentVM - init")
    }
    override fun onCleared() {
        super.onCleared()
    }

    fun setDate(calendar: Calendar){
        Log.d("TAGM", "MonthFragmentVM - setDate")
        _calendarLD.value = calendar
    }

    fun loadEventsForMonth (): LiveData<List<Event>>{
        Log.d("TAGM", "MonthFragmentVM - loadEventsForMonth")
        val formatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val date = formatter.format(calendarLD.value?.time!!)

        val eventsLD = REPOSITORY.eventDao.getEventsForMonth(date)
        mediatorLiveData.addSource(eventsLD){listEventForDB ->
            mediatorLiveData.value = listEventForDB.map { toEvent(it) }
        }
        return mediatorLiveData
    }

    fun createDays(list: List<Event>): List<Day>{

        val  listDays = mutableListOf<Day>()

        val m = list.map {  }

    }

    fun editDay(){

    }

    fun exportReport(){

    }




}