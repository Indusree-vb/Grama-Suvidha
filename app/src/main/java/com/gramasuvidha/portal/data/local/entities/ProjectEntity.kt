package com.gramasuvidha.portal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameKn: String,
    val locationEn: String,
    val locationKn: String,
    val budget: String,
    val expectedCompletionDate: String, // New field for compliance
    val statusEn: String,
    val statusKn: String,
    val completionPercentage: Int,
    val descriptionEn: String,
    val descriptionKn: String,
    val beforeImageUrl: String,
    val afterImageUrl: String
)
