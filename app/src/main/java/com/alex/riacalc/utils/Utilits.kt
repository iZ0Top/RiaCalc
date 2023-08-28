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

val patternDT = "yyyy-MM-dd HH:mm:ss"
val patternD = "yyyy-MM-dd HH:mm:ss"

fun convertDateAndTimeToString(calendar: Calendar): String {
    val formatter = SimpleDateFormat(patternDT, Locale.getDefault())
    return formatter.format(calendar.time)
}
fun convertDateAndTimeToCalendar(string: String): Calendar {
    val formatter = SimpleDateFormat(patternDT, Locale.getDefault())
    val date = formatter.parse(string)
    return Calendar.getInstance().apply { time = date }
}

fun convertDateToString(calendar: Calendar): String {
    val formatter = SimpleDateFormat(patternDT, Locale.getDefault())
    return formatter.format(calendar.time)
}