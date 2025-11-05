import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import ru.itis.android.MessagesRepository
import ru.itis.android.R
import ru.itis.android.model.NotificationModel
import ru.itis.android.model.NotificationPriority
import kotlin.random.Random

@Composable
fun NotifSettingsScreen(
    onButtonClick: (NotificationModel, Boolean, Boolean, Boolean) -> Unit
) {
    val ctx = LocalContext.current
    val notifTitle = remember { mutableStateOf("") }
    val notifText = remember { mutableStateOf("") }
    val notEmptyTitle = remember { mutableStateOf(true) }

    //for switches
    var isOpenedText by remember { mutableStateOf(false) }
    var isClichedToMainActivity by remember { mutableStateOf(false) }
    var isAnswering by remember { mutableStateOf(false) }

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
                text = if (notEmptyTitle.value) {
                    ""
                } else if (!notEmptyTitle.value) {
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
                    notEmptyTitle.value = true
                },
                label = { Text(stringResource(R.string.email_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = notifText.value,
                onValueChange = { input ->
                    notifText.value = input
                },
                label = { Text(stringResource(R.string.password_label)) }
            )

            Spacer(modifier = Modifier.height(50.dp))

            if (notifText.value.isEmpty()){isOpenedText = false}

            Text(text = stringResource(R.string.switch_isOpeningText))
            Switch(
                checked = isOpenedText,
                onCheckedChange = { input ->
                    if (notifText.value.isNotEmpty() || isOpenedText){
                        isOpenedText = input
                        println("TEST TAG: status - ${isOpenedText}")
                    }

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
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(text = stringResource(R.string.switch_isClickingToMainActivity))
            Switch(
                checked = isClichedToMainActivity,
                onCheckedChange = { input ->
                    isClichedToMainActivity = input
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(text = stringResource(R.string.switch_isAnswering))
            Switch(
                checked = isAnswering,
                onCheckedChange = { input ->
                    isAnswering = input
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                if (notifTitle.value.isEmpty()) {
                    if (notifTitle.value.isEmpty()) {
                        notEmptyTitle.value = false
                    }
                } else {
                    onButtonClick.invoke(
                        NotificationModel(
                            id = Random.nextInt(0, 100),
                            title = notifTitle.value,
                            content = notifText.value,
                            priority = selectedPriority
                        ),
                        isOpenedText,
                        isClichedToMainActivity,
                        isAnswering
                    )
                    if (!isAnswering){
                        MessagesRepository.addMessage(
                            title = notifTitle.value,
                            text = notifText.value
                        )
                    }

                }
            }) {
                Text(text = stringResource(R.string.button_from_mainPage_to_viewer))
            }

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}
