import android.app.Notification
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.itis.android.MessagesRepository
import ru.itis.android.R
import ru.itis.android.model.NotificationModel
import ru.itis.android.model.NotificationPriority
import kotlin.random.Random

@Composable
fun NotifSettingsScreen(
    onButtonClick: (NotificationModel) -> Unit
) {
    val ctx = LocalContext.current
    val notifTitle = remember { mutableStateOf("") }
    val notifText = remember { mutableStateOf("") }
    val correctionEmail = remember { mutableStateOf(true) }
    val correctionPassword = remember { mutableStateOf(true) }
    val notEmptyEmail = remember { mutableStateOf(true) }
    val notEmptyPassword = remember { mutableStateOf(true) }

    val isChacked = remember { mutableStateOf(false) }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(NotificationPriority.MEDIUM) }

    Surface (
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (correctionEmail.value && notEmptyEmail.value) {
                    ""
                } else if (!notEmptyEmail.value) {
                    stringResource(R.string.empty_line_error)
                } else {
                    stringResource(R.string.incorrect_email)
                },
                color = Color.Red
            )

            OutlinedTextField(
                value = notifTitle.value,
                onValueChange = { input ->
                    notifTitle.value = input
                    correctionEmail.value = true
                    notEmptyEmail.value = true
                },
                label = { Text(stringResource(R.string.email_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = notifText.value,
                onValueChange = { input ->
                    notifText.value = input
                    correctionPassword.value = true
                    notEmptyPassword.value = true
                },
                label = { Text(stringResource(R.string.password_label)) }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(text = "Switch 1")
            Switch(
                checked = isChacked.value,
                onCheckedChange = { input ->
                    isChacked.value = input
                    println("TEST TAG: status - ${isChacked.value}")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.wrapContentSize()) {
                Button(
                    onClick = { dropdownExpanded = true }
                ) {
                    Text(selectedPriority.getDisplayName(ctx))
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    NotificationPriority.entries.forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.getDisplayName(ctx)) },
                            onClick = {
                                selectedPriority = priority
                                dropdownExpanded = false
                                println("TEST TAG: select priority ${priority.getDisplayName(ctx)}")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "Switch 2")
            Switch(
                checked = isChacked.value,
                onCheckedChange = { input ->
                    isChacked.value = input
                    println("TEST TAG: status - ${isChacked.value}")
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "Switch 3")
            Switch(
                checked = isChacked.value,
                onCheckedChange = { input ->
                    isChacked.value = input
                    println("TEST TAG: status - ${isChacked.value}")
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                if (notifTitle.value.isEmpty()) {
                    if (notifTitle.value.isEmpty()) {
                        notEmptyEmail.value = false
                    }
                } else {
                    onButtonClick.invoke(
                        NotificationModel(
                            id = Random.nextInt(0, 100),
                            title = notifTitle.value,
                            content = notifText.value
                        )
                    )
                    MessagesRepository.addMessage(
                        title = notifTitle.value,
                        text = notifText.value
                    )
                }
            }) {
                Text(text = stringResource(R.string.button_from_mainPage_to_viewer))
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}
