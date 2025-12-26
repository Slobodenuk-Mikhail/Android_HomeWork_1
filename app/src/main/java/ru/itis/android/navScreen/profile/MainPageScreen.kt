import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlinx.serialization.Contextual
import ru.itis.android.data.UserRepository
import ru.itis.android.data.UserSession
import ru.itis.android.model.UserDataModel

@Composable
fun MainPageScreen(
    navController: NavHostController,
    userRepository: UserRepository,
) {
    val currentUsername = UserSession.getCurrentUsername() ?: "Гость"
    val currentUserId = UserSession.getCurrentUserId()

    var userInfo by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val currentText = remember { mutableStateOf("") }
    val counter = remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Добро пожаловать, $currentUsername!",
                style = MaterialTheme.typography.headlineMedium
            )

            if (currentUserId != null) {
                Text(
                    text = "ID пользователя: $currentUserId",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
