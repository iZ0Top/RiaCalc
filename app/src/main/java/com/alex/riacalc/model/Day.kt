package com.alex.riacalc.model

import java.util.Calendar

data class Day(
    val date: Calendar,
    val inspectionCount: Int,
    //---new---
    val inspectionOtherTypesCount: Int,
    //--------
    val inspectionSum: Int,
    val tripCount: Int,
    val tripSum: Int,
    val otherCount: Int,
    val otherSum: Int,
    val list: List<Event>
)