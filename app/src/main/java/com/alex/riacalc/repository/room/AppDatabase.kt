package com.alex.riacalc.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alex.riacalc.model.Event

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getEventDao(): RoomDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            return if (db == null){
                db = Room.databaseBuilder(context, AppDatabase::class.java, "database.db").build()
                db as AppDatabase
            } else {
                db as AppDatabase
            }
        }
    }
}