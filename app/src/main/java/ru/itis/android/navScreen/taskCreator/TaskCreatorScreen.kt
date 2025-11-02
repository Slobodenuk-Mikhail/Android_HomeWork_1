package ru.itis.android.navScreen.taskCreator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.android.Keys
import ru.itis.android.R
import ru.itis.android.model.TaskDataModel

@Composable
fun TaskCreatorScreen(
    navController: NavController
){
    val taskTitle = remember { mutableStateOf("") }
    val taskText = remember { mutableStateOf("") }
    val notEntityTitle = remember { mutableStateOf(true) }

    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(text = if (notEntityTitle.value){
                    ""
                } else {
                    stringResource(R.string.empty_line_error)
                },
                color = Color.Red
            )

            OutlinedTextField(
                value = taskTitle.value,
                onValueChange = {impute ->
                    taskTitle.value = impute
                    notEntityTitle.value = true
                },
                label = {Text(stringResource(R.string.title_label))}
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = taskText.value,
                onValueChange = {impute ->
                    taskText.value = impute
                },
                label = {Text(stringResource(R.string.text_label))}
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                if (taskTitle.value.isEmpty()){
                    notEntityTitle.value = false
                } else {
                    val newTask = TaskDataModel(taskTitle = taskTitle.value, taskText = taskText.value)
                    val taskJson = Json.encodeToString(newTask)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(Keys.TaskCreator.ARRAYLIST_OF_TASKS_FROM_CREATOR_TO_VIEWER, taskJson)

                    navController.popBackStack()
                }

            }
            ) {
                Text(text = stringResource(R.string.button_from_creator_to_viewer))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}