import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.itis.android.navigation.NavigationIds

@Composable
fun MainPageScreen(
    navController: NavHostController
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = "MainScreen")

            Button(onClick = {
                navController.navigate(NavigationIds.VIEW_TASKS.name)
            }) {
                Text(text= "Show Tasks")
            }
        }
    }
}