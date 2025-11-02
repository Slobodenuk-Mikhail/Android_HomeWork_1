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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.itis.android.R
import ru.itis.android.model.NotificationModel
import ru.itis.android.navigation.TaskViewerObject
import ru.itis.android.utils.NotificationHandler
import kotlin.random.Random

@Composable
fun MainPageScreen(
    navController: NavHostController,
    returnedText: String? = null,
    onButtonClick: (NotificationModel) -> Unit
) {
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val correctionEmail = remember { mutableStateOf(true) }
    val correctionPassword = remember { mutableStateOf(true) }
    val notEmptyEmail = remember { mutableStateOf(true) }
    val notEmptyPassword = remember { mutableStateOf(true) }

    Surface (
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {

            returnedText?.let {
                Text(text = it)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = if (correctionEmail.value && notEmptyEmail.value) {
                ""
            } else if (!notEmptyEmail.value){
                stringResource(R.string.empty_line_error)
            } else {
                stringResource(R.string.incorrect_email)
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
                label = {Text(stringResource(R.string.email_label))}
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (correctionPassword.value && notEmptyPassword.value) {
                    ""
                } else if (!notEmptyPassword.value) {
                    stringResource(R.string.empty_line_error)
                } else {
                    stringResource(R.string.password_length_error)
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
                    label = {Text(stringResource(R.string.password_label))},
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
                    stringResource(R.string.hide_password)
                } else {
                    stringResource(R.string.show_password)
                })}
            }


            Spacer(modifier = Modifier.weight(1f))


            Button(onClick = {
//                if(userEmail.value.isEmpty() || userPassword.value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail.value).matches() || userPassword.value.length < 8){
//                    if (userEmail.value.isEmpty()) {
//                        notEmptyEmail.value = false
//                    }
//                    if (userPassword.value.isEmpty()){
//                        notEmptyPassword.value = false
//                    }
//                    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.value).matches()){
//                        correctionEmail.value = false
//                    }
//                    if (userPassword.value.isEmpty() || userPassword.value.length < 8) {
//                        correctionPassword.value = false
//                    }
//                }
//
//                else {
//                    navController.navigate(
//                        route = TaskViewerObject(
//                            userEmail = userEmail.value
//                        )
//                    )
//                }

                onButtonClick.invoke(
                    NotificationModel(
                        id = Random.nextInt(0, 100),
                        title = "Sample title",
                        content = "Sample context"
                    )
                )
            }) {
                Text(text= stringResource(R.string.button_from_mainPage_to_viewer))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}