package com.alex.riacalc.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.logging.SimpleFormatter


fun dateForDatabase(calendar: Calendar): String{
    val formatter = SimpleDateFormat(datePattern, Locale.getDefault())
    return formatter.format(calendar)
}