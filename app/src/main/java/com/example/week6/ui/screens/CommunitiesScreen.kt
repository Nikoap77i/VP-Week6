package com.example.week6.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.week6.data.model.Community
import com.example.week6.ui.components.CommunityIconBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunitiesScreen(
    communities: List<Community>,
    onToggleJoin: (String) -> Unit,
    onOpenProjects: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Communities Directory", fontWeight = FontWeight.Bold)
                        Text(
                            "Pilih komunitas untuk membuka Club Projects",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(communities, key = { it.id }) { c ->
                CommunityCard(
                    community = c,
                    onToggleJoin = { onToggleJoin(c.id) },
                    onOpen = { onOpenProjects(c.id) }
                )
            }
        }
    }
}

@Composable
private fun CommunityCard(
    community: Community,
    onToggleJoin: () -> Unit,
    onOpen: () -> Unit
) {
    Card(
        onClick = onOpen,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CommunityIconBox(community.icon)
                Column {
                    Text(community.name, fontWeight = FontWeight.Bold)
                    Text(
                        "${community.tagline} • ${community.members} Mahasiswa",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(community.description, style = MaterialTheme.typography.bodySmall)
            HorizontalDivider()
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (community.isJoined) {
                    TextButton(onClick = onOpen, contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.LockOpen, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Unlocked • Buka Proyek →", style = MaterialTheme.typography.labelMedium)
                    }
                    OutlinedButton(
                        onClick = onToggleJoin,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Default.Check, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Joined")
                    }
                } else {
                    TextButton(onClick = onOpen, contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.Lock, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Terkunci (Gate Akses)", style = MaterialTheme.typography.labelMedium)
                    }
                    Button(onClick = onToggleJoin) {
                        Icon(Icons.Default.Add, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Join")
                    }
                }
            }
        }
    }
}
