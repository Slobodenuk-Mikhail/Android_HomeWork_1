// navScreen/signUp/SignUpScreen.kt
package ru.itis.android.navScreen.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.itis.android.data.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.UserDataModel
import ru.itis.android.navigation.CatalogObject

@Composable
fun SignUpScreen(navController: NavHostController) {

    // Состояние
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf("") }
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
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Поле логина
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = ""
                success = ""
            },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле пароля
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
                success = ""
            },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Подтверждение пароля
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                error = ""
                success = ""
            },
            label = { Text("Подтвердите пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Сообщения
        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = Color.Red
            )
        }

        if (success.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = success,
                color = Color.Green
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка регистрации
        Button(
            onClick = {
                // Валидация на UI
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    error = "Заполните все поля"
                    return@Button
                }

                if (username.length < 3) {
                    error = "Логин должен быть не менее 3 символов"
                    return@Button
                }

                if (password.length < 4) {
                    error = "Пароль должен быть не менее 4 символов"
                    return@Button
                }

                if (password != confirmPassword) {
                    error = "Пароли не совпадают"
                    return@Button
                }

                scope.launch {
                    loading = true

                    // Проверяем, не занят ли логин
                    val exists = userRepository.isUserExists(username)
                    if (exists) {
                        error = "Пользователь с таким логином уже существует"
                        loading = false
                        return@launch
                    }

                    // Создаем пользователя
                    try {
                        val userData = UserDataModel(
                            username = username,
                            password = password
                        )

                        val userId = userRepository.createNewUser(userData)
                        if (userId != -1) {
                            UserSession.login(userId, username)

                            // Переходим на экран каталога
                            navController.navigate(CatalogObject.route) {
                                popUpTo("signUp") { inclusive = true }
                            }
                        }

                        // Успех
                        success = "Аккаунт создан! Теперь войдите."
                        error = ""
                        password = ""
                        confirmPassword = ""



                    } catch (e: Exception) {
                        error = "Ошибка при создании аккаунта: ${e.message}"
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
                Text("Создать аккаунт")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка назад
        OutlinedButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад ко входу")
        }
    }
}