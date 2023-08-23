package com.alex.riacalc.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class SharedViewModel: ViewModel() {

    private val calendarLiveData = MutableLiveData<Calendar>()

    fun setDate(calendar: Calendar){
        calendarLiveData.value = calendar
    }
    fun getDate(): LiveData<Calendar>{
        return calendarLiveData
    }
}