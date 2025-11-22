package com.trainingproject.habittrackerapp.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.trainingproject.habittrackerapp.domain.models.Habit
import com.trainingproject.habittrackerapp.presentation.components.HabitCard
import com.trainingproject.habittrackerapp.presentation.navigation.Screen
import com.trainingproject.habittrackerapp.presentation.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val habits = viewModel.habits.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.event) {
        val event = state.event ?: return@LaunchedEffect

        when (event) {
            is UiEvent.Navigate -> {
                navController.navigate(event.route) {
                    if (event.route == Screen.AuthScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
            is UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action
                )
            }

            UiEvent.PopBackStack -> navController.popBackStack()
        }
        viewModel.onEventConsumed()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Habits") },
                actions = {
                    IconButton(onClick = viewModel::onLogoutClick) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onAddHabitClick) {
                Icon(Icons.Filled.Add, "Add new habit")
            }
        }
    ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            habits = habits,
            state = state,
            onHabitClick = viewModel::onHabitClick,
            onToggleCompletion = viewModel::onToggleHabitCompletion,
            onReloadQuoteClick = viewModel::loadMotivationQuote
        )
    }
}

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    habits: List<Habit>,
    state: HomeUiState,
    onHabitClick: (String) -> Unit,
    onToggleCompletion: (Habit) -> Unit,
    onReloadQuoteClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            MotivationWidget(
                quote = state.motivationQuote,
                author = state.quoteAuthor,
                isLoading = state.isQuoteLoading,
                onReloadClick = onReloadQuoteClick,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }

        item {
            Text(
                text = "Active Habits",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (habits.isEmpty()) {
            item {
                Text(
                    text = "No habits yet. Click '+' to add one!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                )
            }
        } else {
            items(habits.size, key = { habits[it].id }) { index ->
                HabitCard(
                    habit = habits[index],
                    onHabitClick = { onHabitClick(habits[index].id) },
                    onToggleCompletion = { onToggleCompletion(habits[index]) }
                )
            }
        }
    }
}

@Composable
fun MotivationWidget(
    quote: String,
    author: String,
    isLoading: Boolean,
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MOTIVATION OF THE DAY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                IconButton(onClick = onReloadClick, enabled = !isLoading, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Reload Quote",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else {
                Text(
                    text = "\"$quote\"",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                AnimatedVisibility(
                    visible = author.isNotBlank(),
                    enter = fadeIn(animationSpec = tween(500, delayMillis = 100)),
                    exit = fadeOut(animationSpec = tween(200))
                ) {
                    Text(
                        text = "— $author",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
