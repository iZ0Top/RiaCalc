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
import com.alex.riacalc.utils.TYPE_BONUS
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_PARK
import com.alex.riacalc.utils.TYPE_INSPECTION_CONST_PROGRESS
import com.alex.riacalc.utils.TYPE_INSPECTION_OTHER
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
            REPOSITORY.eventDao.getEventsForMonth(date)                                             //Отримуємо список з бази даних як ЛайвДату

        mediatorLiveData.addSource(eventsLD) { listEventForDB ->                                    //Додаємо в МедіаторЛайвДата джерело - отриману ЛайвДату
            val events = listEventForDB.map { toEvent(it) }
                .toList()                                                                           //Обробка в лямблі даних з джерела, перетворення Івентів
            val days =
                createDays(events).sortedBy { it.date.get(Calendar.DAY_OF_MONTH) }                  //Обробка в лямблі даних з джерела, Створення Днів

            calculateStatistic(days)
            mediatorLiveData.value =
                days                                                                                //Присвоєння МедіаторЛайвДаті списка дні
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

            var bonusCount = 0
            var bonusSum = 0

            var inspCarDCount = 0
            var inspCarPCount = 0
            var inspConstPCount = 0

            for (event in events) {
                when (event.type) {
                    TYPE_INSPECTION -> {
                        inspectionCount++
                        inspectionSum += event.cost
                    }
                    TYPE_INSPECTION_CAR_DEALERSHIP -> {
                        inspectionCount++
                        inspectionSum += event.cost
                        inspCarDCount++
                    }
                    TYPE_INSPECTION_CAR_PARK -> {
                        inspectionCount++
                        inspectionSum += event.cost
                        inspCarPCount++
                    }
                    TYPE_INSPECTION_CONST_PROGRESS -> {
                        inspectionCount++
                        inspectionSum += event.cost
                        inspConstPCount++
                    }
                    TYPE_TRIP -> {
                        tripCount++
                        tripSum += event.cost
                    }
                    TYPE_OTHER -> {
                        otherCount++
                        otherSum += event.cost
                    }
                    TYPE_BONUS -> {
                        bonusCount++
                        bonusSum += event.cost
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

                bonusCount = bonusCount,
                bonusSum = bonusSum,

                inspCarDCount = inspCarDCount,
                inspCarPCount = inspCarPCount,
                inspConstPCount = inspConstPCount,

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
            otherSum = listDays.sumOf { it.otherSum },
            bonusCount = listDays.sumOf { it.bonusCount },
            bonusSum = listDays.sumOf { it.bonusSum },
            inspCarD = listDays.sumOf { it.inspCarDCount },
            inspCarP = listDays.sumOf { it.inspCarPCount },
            inspConstP = listDays.sumOf { it.inspConstPCount }
        )
    }

    fun createReport() {

        val listDays = mediatorLiveData.value
        val statistic = statisticLD.value

        if (listDays == null || statistic == null) return

        val monthNames = application.resources.getStringArray(R.array.month_name_for_picker)

        val date = listDays[0].date

        //якщо є переврки інши типів то додати строку - серед них:

        //якщо є автосалон то додати - Ввтосалон:
        //якщо є автомайданчик, то додати - Автомайданчики:
        //якщо є хід будівництва то додати - Хід Будівництва:
        //якщо є хід будівництва то додати - ... :

        //якщо є витрати на поїздки, то додати їх
        //якщо є інші витрати то додати їх

        val stringBuilder = StringBuilder()
            .append("${monthNames[date.get(Calendar.MONTH)]}. ${date.get(Calendar.YEAR)}")
            .append(application.resources.getString(R.string.template_inspections, statistic.inspectionsCount))

        val inspectionDetails = listOf(
            "Автосалон" to statistic.inspCarD,
            "Автомайданчик" to statistic.inspCarP,
            "Хід будівництва" to statistic.inspConstP,
        ).filter {
            it.second != 0
        }

        if (inspectionDetails.isNotEmpty()) {
            stringBuilder.append("\nСеред них:")
            inspectionDetails.forEach { (name, count) ->
                stringBuilder.append("\n$name: $count")
            }
        }

        if (statistic.tripsCount != 0) {
            stringBuilder.append(application.resources.getString(R.string.template_trips, statistic.tripsSum))
        }
        if (statistic.otherCount != 0) {
            stringBuilder.append(application.resources.getString(R.string.template_expenses, statistic.otherSum))
        }

        for (day in listDays) {

            val dayEventsList = day.list
            stringBuilder.append(
                application.resources.getString(
                    R.string.template_report_item,
                    convertDateToString(day.date, PATTERN_DATE_D_M_Y),
                    day.inspectionCount
                )
            )

            if (day.inspCarDCount != 0 || day.inspCarPCount !=0 || day.inspConstPCount !=0){
                var count = 0
                for (event in dayEventsList){
                    when (event.type){
                        TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_OTHER -> {
                            if (count == 0){
                                stringBuilder.append(", серед них ${event.description}")
                            }
                            else {
                                stringBuilder.append(" + ${event.description}")
                            }
                            count++
                        }
                    }
                }
            }

            if (day.tripCount != 0 || day.tripSum != 0) {
                for (event in dayEventsList) {
                    when (event.type) {
                        TYPE_TRIP, TYPE_OTHER -> {
                            stringBuilder.append(", ${event.description} -${event.cost}")
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