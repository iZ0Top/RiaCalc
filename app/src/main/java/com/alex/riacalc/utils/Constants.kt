package com.alex.riacalc.utils

import com.alex.riacalc.MainActivity
import com.alex.riacalc.repository.room.RoomRepository

lateinit var APP: MainActivity
lateinit var REPOSITORY: RoomRepository

const val TYPE_INSPECTION = 0
const val TYPE_TRIP = 1
const val TYPE_OTHER = 2

const val KEY_ARGUMENTS_TO_MONTH = "key_argument_to_month"
const val KEY_ARGUMENTS_TO_DAY = "key_argument_to_day"

const val PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
const val PATTERN_DATE = "yyyy-MM-dd"



