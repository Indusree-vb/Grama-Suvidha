package com.gramasuvidha.portal.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectDetailViewModel(private val repository: ProjectRepository) : ViewModel() {

    private val _project = MutableStateFlow<ProjectEntity?>(null)
    val project: StateFlow<ProjectEntity?> = _project

    private val _feedback = MutableStateFlow<List<com.gramasuvidha.portal.data.local.entities.Feedback>>(emptyList())
    val feedback: StateFlow<List<com.gramasuvidha.portal.data.local.entities.Feedback>> = _feedback

    fun loadProject(projectId: String) {
        viewModelScope.launch {
            _project.value = repository.getProjectById(projectId)
            repository.getFeedbackForProject(projectId).collect {
                _feedback.value = it
            }
        }
    }

    fun updateProjectPhotos(beforeUri: String?, afterUri: String?) {
        val currentProject = _project.value ?: return
        val updatedProject = currentProject.copy(
            beforeImageUrl = beforeUri ?: currentProject.beforeImageUrl,
            afterImageUrl = afterUri ?: currentProject.afterImageUrl
        )
        viewModelScope.launch {
            repository.insertProject(updatedProject)
            _project.value = updatedProject
        }
    }
}
