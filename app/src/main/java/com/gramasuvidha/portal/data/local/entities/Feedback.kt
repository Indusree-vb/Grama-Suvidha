package com.gramasuvidha.portal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: String,
    val rating: Int,
    val issueDescription: String,
    val isIssue: Boolean = false,
    val citizenName: String = "Anonymous",
    val timestamp: Long = System.currentTimeMillis()
)
