package com.example.week6.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week6.data.SampleData
import com.example.week6.data.model.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

data class CampusUiState(
    val profile: StudentProfile,
    val communities: List<Community>,
    val projects: List<Project>
) {
    val joinedCommunities: List<Community>
        get() = communities.filter { it.isJoined }

    val joinedProjects: List<Project>
        get() {
            val ids = joinedCommunities.map { it.id }.toSet()
            return projects.filter { it.communityId in ids }
        }

    val joinedCount: Int get() = joinedCommunities.size
    val totalProjects: Int get() = joinedProjects.size
}

data class AddProjectFormState(
    val isVisible: Boolean = false,
    val title: String = "",
    val description: String = "",
    val selectedTech: Set<String> = emptySet()
) {
    val isValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank() && selectedTech.isNotEmpty()
}

class CampusViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CampusUiState(
            profile = SampleData.profile,
            communities = SampleData.communities,
            projects = SampleData.projects
        )
    )
    val uiState: StateFlow<CampusUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(AddProjectFormState())
    val formState: StateFlow<AddProjectFormState> = _formState.asStateFlow()

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    fun toggleJoin(communityId: String) {
        _uiState.update { state ->
            state.copy(communities = state.communities.map {
                if (it.id == communityId) it.copy(isJoined = !it.isJoined) else it
            })
        }
    }

    fun canOpenCommunity(communityId: String): Boolean {
        val joined = _uiState.value.communities.firstOrNull { it.id == communityId }?.isJoined == true
        if (!joined) _messages.trySend("Harap bergabung ke komunitas terlebih dahulu!")
        return joined
    }

    fun deleteProject(projectId: String) {
        _uiState.update { state -> state.copy(projects = state.projects.filterNot { it.id == projectId }) }
    }

    fun setProjectCompleted(projectId: String, completed: Boolean) {
        val newStatus = if (completed) ProjectStatus.COMPLETED else ProjectStatus.IN_PROGRESS
        _uiState.update { state ->
            state.copy(projects = state.projects.map {
                if (it.id == projectId) it.copy(status = newStatus) else it
            })
        }
    }

    fun addProject(communityId: String) {
        val form = _formState.value
        if (!form.isValid) return
        val project = Project(
            id = "proj-$communityId-${System.currentTimeMillis()}",
            communityId = communityId,
            title = form.title.trim(),
            description = form.description.trim(),
            techStack = SampleData.techOptions.filter { it in form.selectedTech }
        )
        _uiState.update { it.copy(projects = it.projects + project) }
        dismissAddDialog()
    }

    fun showAddDialog() = _formState.update { AddProjectFormState(isVisible = true) }
    fun dismissAddDialog() = _formState.update { AddProjectFormState() }
    fun onTitleChange(value: String) = _formState.update { it.copy(title = value) }
    fun onDescriptionChange(value: String) = _formState.update { it.copy(description = value) }
    fun toggleTech(tech: String) = _formState.update {
        it.copy(
            selectedTech = if (tech in it.selectedTech) it.selectedTech - tech else it.selectedTech + tech
        )
    }
}
