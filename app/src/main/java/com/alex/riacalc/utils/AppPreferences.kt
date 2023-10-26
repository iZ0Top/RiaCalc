package com.alex.riacalc.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private lateinit var mPreferences: SharedPreferences

    fun getPreferences(context: Context): SharedPreferences {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mPreferences
    }

    fun setReviewDefaultCost(value: Int){
        mPreferences.edit().putInt(PREF_DEFAULT_COST, value).apply()
    }
    fun setReviewCarDealershipCost(value: Int){
        mPreferences.edit().putInt(PREF_DEFAULT_CAR_DEALERSHIP_COST, value).apply()
    }

    fun getReviewDefaultCost(): Int{
        return mPreferences.getInt(PREF_DEFAULT_COST, 0)
    }
    fun getReviewCarDealershipCost(): Int{
        return mPreferences.getInt(PREF_DEFAULT_CAR_DEALERSHIP_COST, 0)
    }

}