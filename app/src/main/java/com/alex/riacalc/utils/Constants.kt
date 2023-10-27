package com.alex.riacalc.utils

import com.alex.riacalc.MainActivity
import com.alex.riacalc.repository.room.RoomRepository

lateinit var REPOSITORY: RoomRepository

const val TYPE_INSPECTION = 0
const val TYPE_INSPECTION_CAR_DEALERSHIP = 1
const val TYPE_TRIP = 2
const val TYPE_OTHER = 3

const val KEY_ARGUMENTS_TO_MONTH = "key_argument_to_month"
const val KEY_ARGUMENTS_TO_DAY = "key_argument_to_day"

const val PATTERN_DATE_Y_M_D_H_S = "yyyy-MM-dd HH:mm:ss"
const val PATTERN_DATE_Y_M_D = "yyyy-MM-dd"
const val PATTERN_DATE_Y_M = "yyyy-MM"
const val PATTERN_DATE_D_M_Y = "dd.MM.yyyy"

const val PREF_NAME = "pref_name"
const val PREF_DEFAULT_COST = "default_cost"
const val PREF_DEFAULT_CAR_DEALERSHIP_COST = "default_car_dealership_cost"


