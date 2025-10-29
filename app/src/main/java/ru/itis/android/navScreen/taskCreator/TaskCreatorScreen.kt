package ru.itis.android.navScreen.taskCreator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.itis.android.Keys
import ru.itis.android.model.TaskDataModel

@Composable
fun TaskCreatorScreen(
    userEmail: String,
    arrayListOfTasks: ArrayList<TaskDataModel>,
    navController: NavController
){
    val taskTitle = remember { mutableStateOf("") }
    val taskText = remember { mutableStateOf("") }
    val notEntityTitle = remember { mutableStateOf(true) }

    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = "Create new task",
                Modifier.padding(10.dp))

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Здесь будет ошибка",
                color = Color.Red
            )
            OutlinedTextField(
                value = taskTitle.value,
                onValueChange = {impute ->
                    taskTitle.value = impute
                    notEntityTitle.value = true
                },
                label = {Text("Title of task (required)")}
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = taskText.value,
                onValueChange = {impute ->
                    taskText.value = impute
                },
                label = {Text("Text of task")}
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Keys.ViewTasks.TITLE_FROM_CREATOR_TO_VIEWER, taskTitle)
//                    ?.set(Keys.ViewTasks.TEXT_FROM_CREATOR_TO_VIEWER, taskText)
                navController.popBackStack()
            }) {Text(text = "Save")}

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}