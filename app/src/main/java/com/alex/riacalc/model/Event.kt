package com.alex.riacalc.model

import java.io.Serializable
import java.util.Calendar

data class Event(
    val id: Int,
    var type: Int,
    var cost: Int,
    var description: String,
    var date: Calendar
): Serializable