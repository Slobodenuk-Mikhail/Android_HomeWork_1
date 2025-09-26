package com.example.android_homework

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_homework.ui.theme.Android_HomeworkTheme


class FirstActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_HomeworkTheme {
                FirstScreen(
                    onNavigationToSecond = { text ->
                        navigateTo(SecondActivity::class.java, text)
                    },
                    onNavigationToThird = {text ->
                        navigateTo(ThirdActivity::class.java, text)
                    }
                )
            }
        }

    }

    @Composable
    private fun FirstScreen(
        onNavigationToSecond: (String) -> Unit,
        onNavigationToThird: (String) -> Unit

    ) {
        var text by remember { mutableStateOf("") }


        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = text,
                onValueChange = {newText ->
                    text = newText
                },
                label = { Text("Введите текст")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                onNavigationToSecond(text)
                },
            ){
                Text("На второй экран")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onNavigationToThird(text)
                }
            ){
                Text(text = "На третий экран")
            }

        }
    }

    private fun navigateTo(activityClass: Class<*>, text: String) {
        val intent = Intent(this, activityClass)  //разобрать код!!!

        if (text.isNotBlank()) {
            intent.putExtra(EXTRA_TEXT, text)
        }

        startActivity(intent)
    }
}