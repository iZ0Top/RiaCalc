package com.alex.riacalc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: Int,
    var cost: Int,
    var description: String,
    var date: String
): Serializable
