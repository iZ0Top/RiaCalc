package com.alex.riacalc.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun dateAndTimeFormatterForDB(calendar: Calendar): String {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(calendar.time)
}
fun dateFormatterForDB(calendar: Calendar): String {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(calendar.time)
}