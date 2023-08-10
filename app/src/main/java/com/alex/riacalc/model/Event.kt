package com.alex.riacalc.model

import java.io.Serializable

data class Event(
    val id: Int,
    val type: Int,
    var cost: Int,
    var description: String,
    var date: String
): Serializable
