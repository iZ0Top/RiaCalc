package com.alex.riacalc.screens.month

import android.app.Application

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.alex.riacalc.R
import com.alex.riacalc.model.Day
import com.alex.riacalc.model.Event
import com.alex.riacalc.model.Statistic
import com.alex.riacalc.utils.PATTERN_DATE_D_M_Y
import com.alex.riacalc.utils.PATTERN_DATE_Y_M
import com.alex.riacalc.utils.REPOSITORY
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import com.alex.riacalc.utils.convertDateToString
import com.alex.riacalc.utils.toEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MonthFragmentVM(private val application: Application) : AndroidViewModel(application) {


    private var _calendarLD = MutableLiveData<Calendar>()
    private var _statisticLD = MutableLiveData<Statistic>()
    private var _reportLD = MutableLiveData<String>()
    val calendarLD: LiveData<Calendar> get() = _calendarLD
    val statisticLD: LiveData<Statistic> get() = _statisticLD
    val reportLD: LiveData<String> get() = _reportLD

    val mediatorLiveData = MediatorLiveData<List<Day>>()


    fun setDate(calendar: Calendar) {
        _calendarLD.value = calendar
    }

    fun loadEventsForMonth(calendar: Calendar) {
        Log.d("TAGM", "MonthFragmentVM - loadEventsForMonth")
        val formatter = SimpleDateFormat(PATTERN_DATE_Y_M, Locale.getDefault())
        val date = formatter.format(calendar.time)

        val eventsLD =
            REPOSITORY.eventDao.getEventsForMonth(date)                                  //Отримуємо список з бази даних як ЛайвДату

        mediatorLiveData.addSource(eventsLD) { listEventForDB ->                                    //Додаємо в МедіаторЛайвДата джерело - отриману ЛайвДату
            val events = listEventForDB.map { toEvent(it) }
                .toList()                                //Обробка в лямблі даних з джерела, перетворення Івентів
            val days =
                createDays(events).sortedBy { it.date.get(Calendar.DAY_OF_MONTH) }           //Обробка в лямблі даних з джерела, Створення Днів

            calculateStatistic(days)
            mediatorLiveData.value =
                days                                                           //Присвоєння МедіаторЛайвДаті списка дні
        }
    }

    private fun createDays(list: List<Event>): List<Day> {

        val days = list.groupBy { it.date.get(Calendar.DAY_OF_MONTH) }
        val listDays = days.map { (_, events) ->

            var inspectionCount = 0
            var inspectionSum = 0
            var tripCount = 0
            var tripSum = 0
            var otherCount = 0
            var otherSum = 0

            for (event in events) {
                when (event.type) {
                    TYPE_INSPECTION,
                    TYPE_INSPECTION_CAR_DEALERSHIP -> {
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
                list = events
            )
            day
        }
        return listDays.toList()
    }

    private fun calculateStatistic(listDays: List<Day>) {

        _statisticLD.value = Statistic(
            inspectionsCount = listDays.sumOf { it.inspectionCount },
            inspectionsSum = listDays.sumOf { it.inspectionSum },
            tripsCount = listDays.sumOf { it.tripCount },
            tripsSum = listDays.sumOf { it.tripSum },
            otherCount = listDays.sumOf { it.otherCount },
            otherSum = listDays.sumOf { it.otherSum }
        )
    }

    fun createReport() {

        val listDays = mediatorLiveData.value
        val statistic = statisticLD.value

        if (listDays == null || statistic == null) return

        val monthNames = application.resources.getStringArray(R.array.month_name_for_picker)

        val date = listDays[0].date
        val stringBuilder = StringBuilder()
            .append("${monthNames[date.get(Calendar.MONTH)]}. ${date.get(Calendar.YEAR)}")
            .append(application.resources.getString(R.string.template_inspections, statistic.inspectionsCount))
            .append(application.resources.getString(R.string.template_trips, statistic.tripsSum))
            .append(application.resources.getString(R.string.template_expenses, statistic.otherSum))

        for (day in listDays) {
            val dayEventsList = day.list
            stringBuilder.append(
                application.resources.getString(
                    R.string.template_report_item,
                    convertDateToString(day.date, PATTERN_DATE_D_M_Y),
                    day.inspectionCount
                )
            )
            if (day.tripCount != 0 || day.tripSum != 0) {
                for (event in dayEventsList) {
                    when (event.type) {
                        TYPE_TRIP, TYPE_OTHER -> {
                            stringBuilder.append(", ${event.description} - ${event.cost}")
                        }
                    }
                }
            }
        }
        _reportLD.value = stringBuilder.toString()
    }


    override fun onCleared() {
        super.onCleared()
    }
}