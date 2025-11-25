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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.itis.android.MessageModel
import ru.itis.android.MessagesRepository
import ru.itis.android.R

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
                text = stringResource(R.string.user_message_title, message.title),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            message.content?.let {
                Text(text = stringResource(R.string.user_message_text, it))
                Spacer(modifier = Modifier.height(4.dp))
            }

            message.answer?.let {
                Text(
                    text = stringResource(R.string.user_message_answer, it),
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
    var notEmptyTitle by remember { mutableStateOf(true) }

    val messageArray = MessagesRepository.messages

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = if (notEmptyTitle) {
                    ""
                } else {
                    stringResource(R.string.title_line_error)
                },
                color = Color.Red
            )
            OutlinedTextField(
                value = messageTitle,
                onValueChange = { input ->
                    messageTitle = input
                    notEmptyTitle = true
                },
                label = { Text(stringResource(R.string.title_label)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (messageArray.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_messages),
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
                if (messageTitle.isEmpty()){
                    notEmptyTitle = false
                } else {
                    MessagesRepository.addMessage(
                        title = messageTitle
                    )
                    messageTitle = ""
                }

            }) {
                Text(text = stringResource(R.string.button_create_message))
            }
        }
    }
}