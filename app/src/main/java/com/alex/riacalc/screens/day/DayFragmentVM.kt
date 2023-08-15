package com.alex.riacalc.screens.day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.riacalc.model.Event

class DayFragmentVM(application: Application): AndroidViewModel(application) {

    private val context = application

    var eventListLD: LiveData<List<Event>> = MutableLiveData()

    fun initDatabase(){

    }





    override fun onCleared() {
        super.onCleared()
    }
}