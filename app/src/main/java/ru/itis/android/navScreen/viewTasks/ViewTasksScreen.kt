package ru.itis.android.navScreen.viewTasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ViewTasksScreen() {
    Surface {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = "Sample text")
        }
    }
}
