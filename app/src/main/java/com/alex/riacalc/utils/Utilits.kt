package com.alex.riacalc.utils

import com.alex.riacalc.model.Event
import com.alex.riacalc.model.EventForDB
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun toEventForDB(event: Event): EventForDB{
    return EventForDB(
        id = event.id,
        type = event.type,
        cost = event.cost,
        description = event.description,
        date = convertDateAndTimeToString(event.date)
    )
}

fun toEvent(eventForDB: EventForDB): Event{
    return Event(
        id = eventForDB.id,
        type = eventForDB.type,
        cost = eventForDB.cost,
        description = eventForDB.description,
        date = convertDateAndTimeToCalendar(eventForDB.date)
    )
}

fun convertDateAndTimeToString(calendar: Calendar): String {
    val formatter = SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault())
    return formatter.format(calendar.time)
}

fun convertDateToString(calendar: Calendar): String {
    val formatter = SimpleDateFormat(PATTERN_DATE_Y_M_D, Locale.getDefault())
    return formatter.format(calendar.time)
}

fun convertDateForReport(calendar: Calendar): String{
    val formatter = SimpleDateFormat(PATTERN_DATE_D_M_Y, Locale.getDefault())
    return formatter.format(calendar.time)
}

fun convertDateAndTimeToCalendar(string: String): Calendar {
    val formatter = SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault())
    val date = formatter.parse(string)
    return Calendar.getInstance().apply { time = date }
}

