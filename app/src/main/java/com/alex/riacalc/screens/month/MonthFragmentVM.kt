package com.alex.riacalc.screens.month

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.screens.SharedViewModel
import java.util.Calendar

class MonthFragmentVM: ViewModel() {

    private lateinit var sharedViewModel: SharedViewModel

    private var _calendarLD = MutableLiveData<Calendar>()
    val calendarLD: LiveData<Calendar> get() = _calendarLD


    fun initVM(sharedViewModel: SharedViewModel){
        this.sharedViewModel = sharedViewModel
    }

    init {
        getDte()
    }

    override fun onCleared() {
        super.onCleared()
    }

    private fun getDte(){
        _calendarLD = sharedViewModel.getDate() as MutableLiveData<Calendar>
    }

    fun editDay(){

    }

    fun exportReport(){

    }
}