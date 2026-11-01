package com.example.week6.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.week6.data.model.Project
import com.example.week6.data.model.StudentProfile
import com.example.week6.ui.components.*
import com.example.week6.viewmodel.CampusUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: CampusUiState,
    onProjectClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val joined = state.joinedCommunities
    val projects = state.joinedProjects

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CampusTech Hub", fontWeight = FontWeight.Bold)
                        Text(
                            "Student Profile & Portfolio Dashboard",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { ProfileCard(state.profile) }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CounterBadge(
                        value = state.joinedCount,
                        label = "Joined Clubs",
                        caption = "Komunitas yang Diikuti",
                        icon = Icons.Default.Groups,
                        modifier = Modifier.weight(1f)
                    )
                    CounterBadge(
                        value = state.totalProjects,
                        label = "Total Projects",
                        caption = "Seluruh Proyek Dibuat",
                        icon = Icons.Default.Folder,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { SectionTitle("Semua Komunitas yang Diikuti (${joined.size})", Modifier.padding(top = 6.dp)) }
            if (joined.isEmpty()) {
                item { EmptyHint("Belum bergabung dengan komunitas") }
            } else {
                items(joined, key = { "club-${it.id}" }) { c ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CommunityIconBox(c.icon)
                            Column(Modifier.weight(1f)) {
                                Text(c.name, fontWeight = FontWeight.Bold)
                                val count = state.projects.count { it.communityId == c.id }
                                Text(
                                    "${c.tagline} • $count Proyek",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            JoinedPill()
                        }
                    }
                }
            }

            item { SectionTitle("Semua Proyek Mahasiswa (${projects.size})", Modifier.padding(top = 6.dp)) }
            if (projects.isEmpty()) {
                item { EmptyHint("Belum ada proyek yang dicatat") }
            } else {
                items(projects, key = { "proj-${it.id}" }) { p ->
                    val clubName = state.communities.firstOrNull { it.id == p.communityId }?.name.orEmpty()
                    ProjectSummaryCard(p, clubName, onClick = { onProjectClick(p.id) })
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(profile: StudentProfile) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF2563EB))))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(profile.initials, color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold)
            }
            Column {
                Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.2f)) {
                    Text(
                        "MAHASISWA AKTIF",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(profile.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(profile.major, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelMedium)
                Text(profile.nim, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun ProjectSummaryCard(project: Project, clubName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        clubName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(project.title, fontWeight = FontWeight.Bold)
                }
                StatusBadge(project.status)
            }
            Text(
                project.techStack.joinToString(" • "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
