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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_homework.ui.theme.Android_HomeworkTheme


class ThirdActivity : ComponentActivity() {

    private var receivedText: String = "Экран 3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textFromIntent = intent.getStringExtra(EXTRA_TEXT)

        if (!textFromIntent.isNullOrBlank()) {
            receivedText = textFromIntent
        }

        setContent {
            Android_HomeworkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThirdScreen(
                        displayText = receivedText,
                        onNavigationToFirst = {navigateToFirst()}
                    )
                }
            }
        }
    }

    @Composable
    private fun ThirdScreen(
        displayText: String,
        onNavigationToFirst: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = displayText,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigationToFirst
            ) {
                Text(text = "На 1 экран")
            }
        }
    }


    private fun navigateToFirst() {
        val intent = Intent(this, FirstActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }
}