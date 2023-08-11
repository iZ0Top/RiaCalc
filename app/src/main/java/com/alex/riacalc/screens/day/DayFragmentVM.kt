package com.alex.riacalc.screens.day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.model.Event
import com.alex.riacalc.repository.Repository

class DayFragmentVM: ViewModel() {

    private val repository = Repository()

    var eventListLD: LiveData<List<Event>> = MutableLiveData()

    init {
        getData()
    }



    fun getData(){
        eventListLD = repository.getEvent()
    }



    override fun onCleared() {
        super.onCleared()
    }
}