package com.alex.riacalc.screens

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.riacalc.model.Day

class SharedVM: ViewModel(){

    var listDaysLD = MutableLiveData<List<Day>>()

}