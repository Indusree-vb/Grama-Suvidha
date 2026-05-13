package com.gramasuvidha.portal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gramasuvidha.portal.data.local.entities.Feedback
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {
    @Query("SELECT * FROM feedback WHERE projectId = :projectId")
    fun getFeedbackForProject(projectId: String): Flow<List<Feedback>>

    @Insert
    suspend fun insertFeedback(feedback: Feedback)
}
