package com.gramasuvidha.portal.data.repository

import com.gramasuvidha.portal.data.local.dao.FeedbackDao
import com.gramasuvidha.portal.data.local.dao.ProjectDao
import com.gramasuvidha.portal.data.local.entities.Feedback
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val feedbackDao: FeedbackDao
) {
    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    fun getProjectsByStatus(status: String): Flow<List<ProjectEntity>> {
        return projectDao.getProjectsByStatus(status)
    }

    suspend fun getProjectById(id: String): ProjectEntity? {
        return projectDao.getProjectById(id)
    }

    suspend fun insertProjects(projects: List<ProjectEntity>) {
        projectDao.insertProjects(projects)
    }

    suspend fun insertProject(project: ProjectEntity) {
        projectDao.insertProject(project)
    }

    suspend fun deleteProject(project: ProjectEntity) {
        projectDao.deleteProject(project)
    }

    suspend fun deleteAllProjects() {
        projectDao.deleteAllProjects()
    }

    fun getFeedbackForProject(projectId: String): Flow<List<Feedback>> {
        return feedbackDao.getFeedbackForProject(projectId)
    }

    suspend fun insertFeedback(feedback: Feedback) {
        feedbackDao.insertFeedback(feedback)
    }
}
