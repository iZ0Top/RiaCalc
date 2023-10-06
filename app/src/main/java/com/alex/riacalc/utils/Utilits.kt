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
        date = convertDateToString(event.date, PATTERN_DATE_Y_M_D_H_S)
    )
}

fun toEvent(eventForDB: EventForDB): Event{
    return Event(
        id = eventForDB.id,
        type = eventForDB.type,
        cost = eventForDB.cost,
        description = eventForDB.description,
        date = convertDateToCalendar(eventForDB.date)
    )
}

fun convertDateToCalendar(string: String): Calendar {
    val formatter = SimpleDateFormat(PATTERN_DATE_Y_M_D_H_S, Locale.getDefault())
    val date = formatter.parse(string)
    return Calendar.getInstance().apply { time = date }
}
fun convertDateToString(calendar: Calendar, pattern: String): String{
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(calendar.time)
}

