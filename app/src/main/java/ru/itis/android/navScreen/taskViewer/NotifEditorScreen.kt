package ru.itis.android.navScreen.taskViewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.serialization.json.Json
import ru.itis.android.Keys
import ru.itis.android.R
import ru.itis.android.model.TaskDataModel
import ru.itis.android.navigation.CustomNavType

@Composable
fun NotifEditorScreen()
{

    var notifId by remember { mutableStateOf("") }
    var notifNewText by remember { mutableStateOf("") }


    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = notifId,
                onValueChange = { input ->
                    notifId = input
                },
                label = { Text(stringResource(R.string.email_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = notifNewText,
                onValueChange = { input ->
                    notifNewText = input
                },
                label = { Text(stringResource(R.string.password_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {

            }) {
                Text(text = "Обновить уведомление")
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(onClick = {

            }) {
                Text(text = "Обновить уведомление")
            }
        }
    }
}
