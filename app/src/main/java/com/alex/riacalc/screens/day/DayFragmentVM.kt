package com.alex.riacalc.screens.day

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alex.riacalc.model.Event
import com.alex.riacalc.repository.room.AppDatabase
import com.alex.riacalc.repository.room.RoomRepository
import com.alex.riacalc.utils.REPOSITORY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class DayFragmentVM(application: Application) : AndroidViewModel(application) {

    private val context = application

    private var _eventListLD: LiveData<List<Event>> = MutableLiveData()
    val eventListLD: LiveData<List<Event>> get() = _eventListLD
    var date: LiveData<Calendar> = MutableLiveData()

    init {
        Log.d("TAG", "DayFragmentVM - init")
        initDatabase()
        loadEvents()
    }

    private fun initDatabase() {
        Log.d("TAG", "DayFragmentVM - initDatabase")
        val dao = AppDatabase.getInstance(context).getEventDao()
        REPOSITORY = RoomRepository(dao)
    }

    private fun loadEvents(){
        _eventListLD = REPOSITORY.eventDao.getAllEvents()
    }

    fun insertEvent(event: Event){
        viewModelScope.launch (Dispatchers.IO) {
            REPOSITORY.eventDao.insertEvent(event)
        }
    }

    override fun onCleared() {
        super.onCleared()

    }
}