package ru.itis.android.navScreen.taskViewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.itis.android.Keys
import ru.itis.android.model.TaskDataModel
import ru.itis.android.navigation.CustomNavType
import ru.itis.android.navigation.TaskCreatorObject

@Composable
fun Task(
    title: String,
    text: String?
) {
    Column {
        Text(text = title, fontSize = 18.sp, )
        Text(text = text ?: "", fontSize = 14.sp)
    }
}

@Composable
fun TaskViewerScreen(
    userEmail: String,
    navController: NavController
) {
    val tasks = rememberSaveable(
        saver = CustomNavType.TaskListSaver
    ) { mutableStateListOf() }

    val taskJson = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>(Keys.TaskCreator.ARRAYLIST_OF_TASKS_FROM_CREATOR_TO_VIEWER)

    taskJson?.let { json ->
        tasks.add(Json.decodeFromString<TaskDataModel>(taskJson))
    }


    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = userEmail, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            if (tasks.isNotEmpty()){
                Text(text = "Success")

                LazyColumn {
                    items(tasks){ task ->
                        Task(
                            title = task.taskTitle,
                            text = task.taskText
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }

            }


            Button(onClick = {
                navController.navigate(route = TaskCreatorObject)
            }) {
                Text(text = "Create new task")
            }
        }
    }
}
