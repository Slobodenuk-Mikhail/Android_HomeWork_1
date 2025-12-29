package ru.itis.android.navScreen.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.itis.android.R
import ru.itis.android.utils.ResManager
import ru.itis.android.data.users.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.UserDataModel
import ru.itis.android.navigation.CatalogObject

@Composable
fun SignUpScreen(
    navController: NavHostController,
    resManager: ResManager
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val userRepository = remember { ServiceLocator.getUserRepository() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.signup_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = ""
                success = ""
            },
            label = { Text(stringResource(R.string.login_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
                success = ""
            },
            label = { Text(stringResource(R.string.password_hint)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                error = ""
                success = ""
            },
            label = { Text(stringResource(R.string.confirm_password_hint)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

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
                    error = resManager.getString(R.string.validation_empty)
                    return@Button
                }

                if (username.length < 3) {
                    error = resManager.getString(R.string.username_length_error)
                    return@Button
                }

                if (password.length < 4) {
                    error = resManager.getString(R.string.password_length_error)
                    return@Button
                }

                if (password != confirmPassword) {
                    error = resManager.getString(R.string.password_mismatch)
                    return@Button
                }

                scope.launch {
                    loading = true

                    val exists = userRepository.isUserExists(username)
                    if (exists) {
                        error = resManager.getString(R.string.user_exists)
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
                        if (userId > 0) {
                            UserSession.login(userId, username)

                            navController.navigate(CatalogObject.route) {
                                // Очищаем весь стек навигации
                                popUpTo(0) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            error = resManager.getString(R.string.account_creation_error)
                        }
                    } catch (e: Exception) {
                        error = resManager.getString(R.string.generic_error, e.message ?: "")
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
                Text(stringResource(R.string.create_account_button))
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
            Text(stringResource(R.string.back_to_signin))
        }
    }
}