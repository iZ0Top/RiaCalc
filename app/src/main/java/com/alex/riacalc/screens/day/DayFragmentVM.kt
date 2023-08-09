package com.alex.riacalc.screens.day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.model.Event
import java.util.Calendar

class DayFragmentVM: ViewModel() {

    private val _eventListLD = MutableLiveData<List<Event>>()
    val eventListLD: LiveData<List<Event>> = _eventListLD
    private var currentDate: Calendar = Calendar.getInstance()



    override fun onCleared() {
        super.onCleared()
    }
}