package com.alex.riacalc.screens.month

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.screens.SharedViewModel
import java.util.Calendar

class MonthFragmentVM: ViewModel() {

    private lateinit var sharedViewModel: SharedViewModel

    private val _calendarLD = MutableLiveData<Calendar>()
    val calendarLD: LiveData<Calendar> get() = _calendarLD


    fun initVM(sharedViewModel: SharedViewModel){
        this.sharedViewModel = sharedViewModel
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getDte()

    fun editDay(){

    }

    fun exportReport(){

    }
}