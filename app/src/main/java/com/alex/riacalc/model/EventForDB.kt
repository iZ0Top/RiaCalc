package com.alex.riacalc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "events")
data class EventForDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: Int,
    var cost: Int,
    var description: String,
    var date: String
): Serializable
