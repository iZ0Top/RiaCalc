package com.alex.riacalc.model

data class Day(
    val date: Int,
    val inspectionCount: Int,
    val inspectionSum: Int,
    val tripCount: Int,
    val tripSum: Int,
    val otherCount: Int,
    val otherSum: Int,
    val list: List<Event>
)