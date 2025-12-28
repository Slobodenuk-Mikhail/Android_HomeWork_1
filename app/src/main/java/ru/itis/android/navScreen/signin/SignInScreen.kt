package ru.itis.android.navScreen.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.itis.android.data.users.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.navigation.CatalogObject

@Composable
fun SignInScreen(navController: NavHostController) {

    // Состояние
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // Репозиторий
    val userRepository = remember { ServiceLocator.getUserRepository() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Заголовок
        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Поле логина
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = ""
            },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            isError = error.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
            },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            isError = error.isNotEmpty()
        )

        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = Color.Red
            )
        }

        Button(
            onClick = {
                // Валидация на UI уровне
                if (username.isEmpty() || password.isEmpty()) {
                    error = "Заполните все поля"
                    return@Button
                }

                scope.launch {
                    loading = true
                    val userId = userRepository.login(username, password)

                    if (userId != null) {
                        // Успешный вход
                        UserSession.login(userId, username)

                        navController.navigate(CatalogObject.route) {
                            popUpTo("signIn") { inclusive = true }
                        }
                    } else {
                        error = "Неверный логин или пароль"
                    }
                    loading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Войти")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка регистрации
        OutlinedButton(
            onClick = {
                navController.navigate("signUp")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать аккаунт")
        }
    }
}