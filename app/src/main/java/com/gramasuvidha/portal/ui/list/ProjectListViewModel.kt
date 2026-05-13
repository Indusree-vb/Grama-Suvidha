package com.gramasuvidha.portal.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.data.repository.ProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectListViewModel(private val repository: ProjectRepository) : ViewModel() {

    private val _filterStatus = MutableStateFlow<String?>(null)
    val filterStatus: StateFlow<String?> = _filterStatus.asStateFlow()

    val projects: StateFlow<List<ProjectEntity>> = _filterStatus
        .flatMapLatest { status ->
            if (status == null) {
                repository.allProjects
            } else {
                repository.getProjectsByStatus(status)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ongoingCount: StateFlow<Int> = repository.allProjects
        .map { list -> list.count { it.statusEn.equals("In Progress", ignoreCase = true) || it.statusEn.equals("ONGOING", ignoreCase = true) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedCount: StateFlow<Int> = repository.allProjects
        .map { list -> list.count { it.statusEn.equals("Completed", ignoreCase = true) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val plannedCount: StateFlow<Int> = repository.allProjects
        .map { list -> list.count { it.statusEn.equals("Planning", ignoreCase = true) || it.statusEn.equals("PLANNED", ignoreCase = true) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setFilter(status: String?) {
        _filterStatus.value = status
    }

    fun addProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.insertProject(project)
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProject(project)
        }
    }
}
