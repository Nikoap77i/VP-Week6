package com.example.week6.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.week6.navigation.*
import com.example.week6.ui.screens.*
import com.example.week6.viewmodel.CampusViewModel
import kotlinx.coroutines.launch

private data class TopLevelItem(val label: String, val route: Any, val icon: ImageVector)

private val topLevelItems = listOf(
    TopLevelItem("Dashboard", DashboardRoute, Icons.Default.Home),
    TopLevelItem("Communities", CommunitiesRoute, Icons.Default.Groups)
)

@Composable
fun CampusTechApp(
    viewModel: CampusViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.messages.collect { message ->
            snackbarHostState.currentSnackbarData?.dismiss()
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = currentDestination?.let { dest ->
        topLevelItems.any { item -> dest.hierarchy.any { it.hasRoute(item.route::class) } }
    } == true

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelItems.forEach { item ->
                        val selected = currentDestination?.hierarchy
                            ?.any { it.hasRoute(item.route::class) } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<DashboardRoute> {
                DashboardScreen(
                    state = uiState,
                    onProjectClick = { navController.navigate(ProjectDetailRoute(it)) }
                )
            }

            composable<CommunitiesRoute> {
                CommunitiesScreen(
                    communities = uiState.communities,
                    onToggleJoin = viewModel::toggleJoin,
                    onOpenProjects = { id ->
                        if (viewModel.canOpenCommunity(id)) {
                            navController.navigate(ClubProjectsRoute(communityId = id))
                        }
                    }
                )
            }

            composable<ClubProjectsRoute> { entry ->
                val route = entry.toRoute<ClubProjectsRoute>()
                ClubProjectsScreen(
                    community = uiState.communities.firstOrNull { it.id == route.communityId },
                    projects = uiState.projects.filter { it.communityId == route.communityId },
                    form = formState,
                    onBack = { navController.popBackStack() },
                    onProjectClick = { navController.navigate(ProjectDetailRoute(it)) },
                    onDeleteProject = viewModel::deleteProject,
                    onAddClick = viewModel::showAddDialog,
                    onTitleChange = viewModel::onTitleChange,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    onToggleTech = viewModel::toggleTech,
                    onCancel = viewModel::dismissAddDialog,
                    onSave = { viewModel.addProject(route.communityId) }
                )
            }

            composable<ProjectDetailRoute> { entry ->
                val route = entry.toRoute<ProjectDetailRoute>()
                val project = uiState.projects.firstOrNull { it.id == route.projectId }
                ProjectDetailScreen(
                    project = project,
                    community = uiState.communities.firstOrNull { it.id == project?.communityId },
                    onBack = { navController.popBackStack() },
                    onCompletedChange = { viewModel.setProjectCompleted(route.projectId, it) }
                )
            }
        }
    }
}
