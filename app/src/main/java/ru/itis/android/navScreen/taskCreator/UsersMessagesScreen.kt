package ru.itis.android.navScreen.taskCreator

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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.android.Keys
import ru.itis.android.MessagesRepository
import ru.itis.android.R
import ru.itis.android.model.TaskDataModel



@Composable
fun UsersMessagesScreen(){

    var messageText by remember { mutableStateOf("") }

    val messageArray = MessagesRepository.messages

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))


            OutlinedTextField(
                value = messageText,
                onValueChange = { input ->
                    messageText = input
                },
                label = { Text(stringResource(R.string.email_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (messageArray.isNotEmpty()){
                Text(text = stringResource(R.string.title_list_of_tasks), fontSize = 20.sp)

                Spacer(modifier = Modifier.height(15.dp))

                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(messageArray){ message ->
                        Text(text = message.title)
                        message.content?.let { Text(text = it) }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {

            }) {
                Text(text = stringResource(R.string.button_from_viewer_to_creator))
            }
        }
    }
}