package com.alex.riacalc.model

import java.util.Calendar

data class Day(
    val date: Calendar,
    val inspectionCount: Int,
    val inspectionSum: Int,
    val tripCount: Int,
    val tripSum: Int,
    val otherCount: Int,
    val otherSum: Int,
    //
    val bonusCount: Int,
    val bonusSum: Int,
    //
    val inspCarDCount: Int,
    val inspCarPCount: Int,
    val inspConstPCount: Int,
    //
    val list: List<Event>


)