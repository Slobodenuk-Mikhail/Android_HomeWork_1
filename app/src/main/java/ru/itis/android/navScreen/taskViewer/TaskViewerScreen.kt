package ru.itis.android.navScreen.taskViewer

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.itis.android.Keys
import ru.itis.android.model.TaskDataModel

@Composable
fun ViewTasksScreen(
    firstArg: String,
    secondArg: String,
    taskDataModel: TaskDataModel? = null,
    navController: NavController
) {
    val textToMainPage = remember { mutableStateOf("") }
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = firstArg)
            Text(text = secondArg)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textToMainPage.value,
                onValueChange = { imput ->
                    textToMainPage.value = imput
                }
            )

            Button(onClick = {
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Keys.ViewTasks.TEXT_FROM_TASKS_TO_MAIN_PAGE, textToMainPage)
                navController.popBackStack()
            }) {
                Text(text = "To Main Page")
            }
        }
    }
}
