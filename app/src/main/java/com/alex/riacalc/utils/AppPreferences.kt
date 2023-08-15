package com.alex.riacalc.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private const val PREF_NAME = "pref_name"
    private const val PREF_DEFAULT_COST = "default_cost"
    private const val PREF_SHOW_COST = "show_cost"
    private const val PREF_EXPORT_TRIPS = "export_trips"
    private const val PREF_EXPORT_OTHER = "export_other"


    private lateinit var mPreferences: SharedPreferences

    fun getPreferences(context: Context): SharedPreferences {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mPreferences
    }

    fun setReviewDefaultCost(value: Int){
        mPreferences.edit().putInt(PREF_DEFAULT_COST, value).apply()
    }
    fun setShowCost(value: Boolean){
        mPreferences.edit().putBoolean(PREF_SHOW_COST, value).apply()
    }
    fun setExportTrips(value: Boolean){
        mPreferences.edit().putBoolean(PREF_EXPORT_TRIPS, value).apply()
    }
    fun setExportOther(value: Boolean){
        mPreferences.edit().putBoolean(PREF_EXPORT_OTHER, value).apply()
    }


    fun getReviewDefaultCost(): Int{
        return mPreferences.getInt(PREF_DEFAULT_COST, 0)
    }
    fun getShowCost(): Boolean{
        return mPreferences.getBoolean(PREF_SHOW_COST, false)
    }

    fun getExportTrips(): Boolean{
        return mPreferences.getBoolean(PREF_EXPORT_TRIPS, false)
    }
    fun getExportOther(): Boolean{
        return mPreferences.getBoolean(PREF_EXPORT_OTHER, false)
    }
}