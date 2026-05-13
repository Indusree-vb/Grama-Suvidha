package com.gramasuvidha.portal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val fullName: String,
    val phone: String,
    val password: String,
    val role: String // "Admin" or "Farmer"
)
