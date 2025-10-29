import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.itis.android.model.TaskDataModel
import ru.itis.android.navigation.TaskViewerObject

@Composable
fun MainPageScreen(
    navController: NavHostController,
    returnedText: String? = null,
) {
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val correctionEmail = remember { mutableStateOf(true) }
    val correctionPassword = remember { mutableStateOf(true) }
    val notEmptyEmail = remember { mutableStateOf(true) }
    val notEmptyPassword = remember { mutableStateOf(true) }

    val arrayListOfTasks: ArrayList<TaskDataModel> = ArrayList()

    Surface (
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {

            returnedText?.let {
                Text(text = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Text(text = "Main Page")

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = if (correctionEmail.value && notEmptyEmail.value) {
                ""
            } else if (!notEmptyEmail.value){
                "Недопустима пустая строка"
            } else {
                "Некорректный Email"
            },
                color = Color.Red
            )

            OutlinedTextField(
                value = userEmail.value,
                onValueChange = { input ->
                    userEmail.value = input
                    correctionEmail.value = true
                    notEmptyEmail.value = true
                },
                label = {Text("Email (required)")}
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (correctionPassword.value && notEmptyPassword.value) {
                    ""
                } else if (!notEmptyPassword.value) {
                    "Недопустима пустая строка"
                } else {
                    "Минимальная длина пароля 8 символов"
                },
                color = Color.Red
            )

            Row {
                OutlinedTextField(
                    value = userPassword.value,
                    onValueChange = { input ->
                        userPassword.value = input
                        correctionPassword.value = true
                        notEmptyPassword.value = true
                    },
                    label = {Text("Password (required)")},
                    visualTransformation = if (passwordVisible.value) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }

                )

                Button(onClick = {
                    passwordVisible.value = !passwordVisible.value
                }
                ) {Text(text = if (passwordVisible.value) {
                    "Скрыть пароль"
                } else {
                    "Показать пароль"
                })}
            }


            Spacer(modifier = Modifier.weight(1f))


            Button(onClick = {
                if(userEmail.value.isEmpty() || userPassword.value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail.value).matches() || userPassword.value.length < 8){
                    if (userEmail.value.isEmpty()) {
                        notEmptyEmail.value = false
                    }
                    if (userPassword.value.isEmpty()){
                        notEmptyPassword.value = false
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.value).matches()){
                        correctionEmail.value = false
                    }
                    if (userPassword.value.isEmpty() || userPassword.value.length < 8) {
                        correctionPassword.value = false
                    }
                }

                else {
                    navController.navigate(
                        route = TaskViewerObject(
                            userEmail = userEmail.value,
                            arrayListOfTasks = arrayListOfTasks
                        )
                    )
                }
            }) {
                Text(text= "Show Tasks")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}