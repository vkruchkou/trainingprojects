package com.trainingproject.habittrackerapp.presentation.components

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trainingproject.habittrackerapp.domain.models.Habit

@Composable
fun HabitCard(
    habit: Habit,
    onHabitClick: (String) -> Unit,
    onToggleCompletion: (Habit) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompletedToday = habit.isCompletedToday()

    val scale = animateFloatAsState(
        targetValue = if (isCompletedToday) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "checkAnimationScale"
    ).value

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onHabitClick(habit.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (habit.iconUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(habit.iconUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Habit Icon",
                        contentScale = ContentScale.Crop,
                        onError = { error ->
                            Log.e(
                                "CoilError",
                                "Failed to load image from URL: ${habit.iconUrl}, Error: ${error.result.throwable.localizedMessage}"
                            )
                        },
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Habit Icon",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${habit.getProgress()} / ${habit.goalDays} days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = { onToggleCompletion(habit) },
                    modifier = Modifier.size(44.dp)
                ) {
                    if (isCompletedToday) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Mark as complete",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scale(scale)
                        )
                    } else {
                        OutlinedButton(
                            onClick = { onToggleCompletion(habit) },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                        }
                    }
                }
            }
        }
    }
}