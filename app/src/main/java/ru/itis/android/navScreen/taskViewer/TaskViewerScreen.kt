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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.itis.android.model.TaskDataModel
import ru.itis.android.navigation.TaskCreatorObject

@Composable
fun Task(
    title: String,
    text: String?
) {
    Column {
        Text(text = title, fontSize = 18.sp)
        Text(text = text ?: "", fontSize = 14.sp)
    }
}
@Composable
fun TaskViewerScreen(
    userEmail: String,
    arrayListOfTasks: ArrayList<TaskDataModel>? = null,
    navController: NavController
) {



    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = userEmail, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            if (arrayListOfTasks?.isNotEmpty() ?: false){

                Text(text = "Success")

                LazyColumn {
                    items(arrayListOfTasks){ task ->
                        Task(
                            title = task.taskTitle,
                            text = task.taskText
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }

            }


            Button(onClick = {
                navController.navigate(
                    route = TaskCreatorObject(
                        arrayListOfTasks = arrayListOfTasks ?: ArrayList()
                    )
                )
            }) {
                Text(text = "Create new task")
            }
        }
    }
}
