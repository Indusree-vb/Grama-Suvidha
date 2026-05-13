package com.gramasuvidha.portal.data.model

data class Project(
    val id: String,
    val nameEn: String,
    val nameKn: String,
    val budget: String,
    val statusEn: String,
    val statusKn: String,
    val completionPercentage: Int,
    val descriptionEn: String,
    val descriptionKn: String,
    val beforeImageUrl: String,
    val afterImageUrl: String
)
