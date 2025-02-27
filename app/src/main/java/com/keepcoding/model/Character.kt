package com.keepcoding.model

class Character(
    val id: String,
    val name: String,
    val imageUrl: String,
    val totalLife: Int,
    var currentLife: Int = 100,
    var timesSelected: Int = 0
) {
    fun isDead(): Boolean {
        return currentLife <= 0
    }
}

