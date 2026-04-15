import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.oleksandra.pam.lab06.TodoTask
import pl.oleksandra.pam.lab06.Priority

@Composable
fun TaskListItem(item: TodoTask) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Deadline: ${item.deadline}", style = MaterialTheme.typography.bodyMedium)
                }
                Icon(
                    imageVector = if (item.isDone) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = if (item.isDone) Color(0xFF2E7D32) else Color(0xFFB71C1C),
                    modifier = Modifier.size(32.dp)
                )
            }
            Badge(
                containerColor = when(item.priority) {
                    Priority.High -> Color.Red
                    Priority.Medium -> Color.Yellow
                    Priority.Low -> Color.Green
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(item.priority.name, color = Color.Black)
            }
        }
    }
}