package ru.itis.android.navScreen.taskCreator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.android.Keys
import ru.itis.android.MessageModel
import ru.itis.android.MessagesRepository
import ru.itis.android.R
import ru.itis.android.model.TaskDataModel

@Composable
fun MessageCard(message: MessageModel){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Заголовок: ${message.title}",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            message.content?.let {
                Text(text = "Текст: $it")
                Spacer(modifier = Modifier.height(4.dp))
            }

            message.answer?.let {
                Text(
                    text = "Ответ: $it",
                    color = Color.Blue,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun UsersMessagesScreen(){

    var messageTitle by remember { mutableStateOf("") }

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
                value = messageTitle,
                onValueChange = { input ->
                    messageTitle = input
                },
                label = { Text(stringResource(R.string.email_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (messageArray.isEmpty()) {
                Text(
                    text = "Нет сообщений",
                    modifier = Modifier.padding(16.dp)
                )
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(messageArray){ message ->
                        MessageCard(message = message)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                MessagesRepository.addMessage(
                    title = messageTitle
                )
                messageTitle = ""
            }) {
                Text(text = stringResource(R.string.button_from_viewer_to_creator))
            }
        }
    }
}