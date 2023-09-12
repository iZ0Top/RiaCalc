package com.alex.riacalc.screens.month

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.model.Day
import com.alex.riacalc.model.Event
import com.alex.riacalc.model.EventForDB
import com.alex.riacalc.utils.PATTERN_DATE_Y_M
import com.alex.riacalc.utils.REPOSITORY
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import com.alex.riacalc.utils.toEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthFragmentVM: ViewModel() {


    private var _calendarLD = MutableLiveData<Calendar>()
    val calendarLD: LiveData<Calendar> get() = _calendarLD
    val mediatorLiveData = MediatorLiveData<List<Day>>()

    fun setDate(calendar: Calendar){
        _calendarLD.value = calendar
    }


    fun loadEventsForMonth (calendar: Calendar){
        Log.d("TAGM", "MonthFragmentVM - loadEventsForMonth")
        val formatter = SimpleDateFormat(PATTERN_DATE_Y_M, Locale.getDefault())
        val date = formatter.format(calendar.time)

        val eventsLD = REPOSITORY.eventDao.getEventsForMonth(date)                                  //Отримуємо список з бази даних як ЛайвДату

        mediatorLiveData.addSource(eventsLD){listEventForDB ->                                      //Додаємо в МедіаторЛайвДата джерело - отриману ЛайвДату
            val listEvent = listEventForDB.map { toEvent(it) }.toList()                             //Обробка в лямблі даних з джерела, перетворення Івентів
            val listDay = createDays(listEvent).sortedBy { it.date.get(Calendar.DAY_OF_MONTH) }     //Обробка в лямблі даних з джерела, Створення Днів
            mediatorLiveData.value = listDay                                                        //Присвоєння МедіаторЛайвДаті списка днів
        }
    }

    private fun createDays(list: List<Event>): List<Day>{

        val days = list.groupBy { it.date.get(Calendar.DAY_OF_MONTH) }
        val listDays = days.map {(_, events) ->

            var inspectionCount = 0
            var inspectionSum = 0
            var tripCount = 0
            var tripSum = 0
            var otherCount = 0
            var otherSum = 0

            for (event in events){
                when(event.type){
                    TYPE_INSPECTION -> {
                        inspectionCount++
                        inspectionSum += event.cost
                    }
                    TYPE_TRIP -> {
                        tripCount++
                        tripSum += event.cost
                    }
                    TYPE_OTHER -> {
                        otherCount++
                        otherSum += event.cost
                    }
                }
            }
            val day = Day(
                date = events[0].date,
                inspectionCount = inspectionCount,
                inspectionSum = inspectionSum,
                tripCount = tripCount,
                tripSum = tripSum,
                otherCount = otherCount,
                otherSum = otherSum,
                list = events)
            Log.d("TAGM", "createDays: \n $day").toString()
            day
        }
        return listDays.toList()
    }

    fun createReport(){
        val listDays = mediatorLiveData.value

    }


    override fun onCleared() {
        super.onCleared()
    }
}