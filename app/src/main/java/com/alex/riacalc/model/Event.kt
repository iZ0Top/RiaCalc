package com.alex.riacalc.model

import java.io.Serializable

data class Event(
    val id: Int,
    val type: Int,
    val price: Int,
    val description: String,
    val date: String
): Serializable
