package com.alex.riacalc.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alex.riacalc.model.Event

@Database(version = 1, entities = [Event::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun getEventsDao()

}