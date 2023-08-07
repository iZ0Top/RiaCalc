package com.alex.riacalc.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alex.riacalc.screens.day.DayFragmentVM

class MyViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return DayFragmentVM() as T
    }
}