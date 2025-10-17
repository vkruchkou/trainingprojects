package com.trainingproject.tasks.uiSreens.screens.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.trainingproject.tasks.data.Task
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun TaskCard(
    task: Task,
    onClick: (Task) -> Unit = {},
    onToggleDone: (Task) -> Unit = {},
    onRemove: (Task) -> Unit = {}
) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    val dueDateText = task.dueDate?.let { dateFormatter.format(Date(it)) }
    val isOverdue = task.dueDate?.let { it < System.currentTimeMillis() && !task.taskDone } ?: false

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )

    val cardVisible = remember { mutableStateOf(true) }
    SwipeToDismissBox(
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        state = dismissState,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Color.Red
                else -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        content = {
            AnimatedVisibility(
                visible = cardVisible.value,
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable { onClick(task) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onToggleDone(task) }) {
                            Icon(
                                imageVector = if (task.taskDone) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = "Task done",
                                tint = if (task.taskDone) Color(0xFF4CAF50) else Color.Gray
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = task.taskName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = if (task.taskDone) FontWeight.Normal else FontWeight.SemiBold
                                ),
                                color = if (task.taskDone) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (task.description.isNotBlank()) {
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (dueDateText != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.DateRange,
                                        contentDescription = "Due date",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (isOverdue) Color.Red else Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = dueDateText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isOverdue) Color.Red else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            cardVisible.value = false
            delay(220)
            onRemove(task)
        }
    }
}