package com.alex.riacalc.repository

interface Repository {

    fun getEvent()

    fun getEventsPerDay()

    fun putEvent()

    fun deleteEvent()

}