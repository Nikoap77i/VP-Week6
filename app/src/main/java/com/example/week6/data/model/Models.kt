package com.example.week6.data.model

enum class CommunityIcon { CODE, SECURITY, AI, MOBILE, CLOUD, DESIGN }

enum class ProjectStatus(val label: String) {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}

data class StudentProfile(
    val name: String,
    val major: String,
    val nim: String,
    val initials: String
)

data class Community(
    val id: String,
    val name: String,
    val fullName: String,
    val tagline: String,
    val description: String,
    val members: Int,
    val icon: CommunityIcon,
    val isJoined: Boolean = false
)

data class Project(
    val id: String,
    val communityId: String,
    val title: String,
    val description: String,
    val techStack: List<String>,
    val status: ProjectStatus = ProjectStatus.IN_PROGRESS
)
