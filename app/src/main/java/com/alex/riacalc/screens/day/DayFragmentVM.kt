package com.alex.riacalc.screens.day

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex.riacalc.model.Event
import com.alex.riacalc.repository.room.AppDatabase
import com.alex.riacalc.repository.room.RoomRepository
import com.alex.riacalc.utils.REPOSITORY

class DayFragmentVM(application: Application): AndroidViewModel(application) {

    private val context = application

    var eventListLD: LiveData<List<Event>> = MutableLiveData()

     fun initDatabase(){
        Log.d("TAG", "DayFragmentVM - initDatabase" )
        val dao = AppDatabase.getInstance(context).getEventDao()
        val repo = RoomRepository(dao)
    }

    override fun onCleared() {
        super.onCleared()
    }
}