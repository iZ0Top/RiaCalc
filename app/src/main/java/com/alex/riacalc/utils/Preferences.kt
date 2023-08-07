package com.alex.riacalc.utils

import android.content.Context

class Preferences (val context: Context) {

    companion object {
        const val KEY_REVIEW_COST = "key_review_cost"
        const val KEY_SHOW_COST = "key_show_cost"
        const val KEY_EXPORT_TRIPS = "key_export_trips"
        const val KEY_EXPORT_OTHER_EXPENSES = "key_export_other_expenses"
    }
}