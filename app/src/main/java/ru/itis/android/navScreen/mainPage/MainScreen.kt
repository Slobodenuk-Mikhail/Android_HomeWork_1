package ru.itis.android

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import kotlin.random.Random

class ToastException(message: String) : Exception(message)
class SnackbarException(message: String) : Exception(message)
class ResetSettingsException(message: String) : Exception(message)

private fun getSelectedDispatcher(name: String): CoroutineDispatcher {
    return when (name) {
        "IO" -> Dispatchers.IO
        "Main" -> Dispatchers.Main
        else -> Dispatchers.Default
    }
}

private suspend fun launchSingleCoroutine(index: Int, context: android.content.Context) {
    val delayTime = (1000L..10000L).random()
    println("Корутина $index: задержка ${delayTime}ms")

    delay(delayTime)

    if (delayTime >= 7000 && Random.nextFloat() < 0.3f) {
        println("Корутина $index: условие для исключения выполнено")

        val exception = when (Random.nextInt(3)) {
            0 -> ToastException(
                context.getString(R.string.error_coroutine_too_long, index, delayTime)
            )
            1 -> SnackbarException(
                context.getString(R.string.error_coroutine_timeout, index, delayTime)
            )
            else -> ResetSettingsException(
                context.getString(R.string.error_reset_settings, index, delayTime)
            )
        }

        throw exception
    }

    println("Корутина $index: успешно завершена за ${delayTime}ms")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var sliderPosition by remember { mutableFloatStateOf(10f) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dispatcherSelect by remember { mutableStateOf(R.string.dispatcher_default) }
    var isSequential by remember { mutableStateOf(true) }
    var isParallel by remember { mutableStateOf(false) }
    var isLazy by remember { mutableStateOf(false) }

    var isProcessing by remember { mutableStateOf(false) }
    var completedCount by remember { mutableStateOf(0) }
    var totalCount by remember { mutableStateOf(0) }

    var executionScope by remember { mutableStateOf<CoroutineScope?>(null) }

    val resetSettings = {
        sliderPosition = 10f
        dispatcherSelect = R.string.dispatcher_default
        isSequential = true
        isParallel = false
        isLazy = false
        println("TEST TAG: Настройки сброшены")
    }

    val handleException = { e: Exception, index: Int ->
        when (e) {
            is ToastException -> {
                coroutineScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        e.message ?: context.getString(R.string.error_default_coroutine, index),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is SnackbarException -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = e.message ?: context.getString(R.string.error_default_coroutine, index),
                        actionLabel = "OK"
                    )
                }
            }
            is ResetSettingsException -> {
                resetSettings()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.snackbar_settings_reset, index),
                        actionLabel = "OK"
                    )
                }
            }
        }
    }

    fun startCoroutines() {
        if (isProcessing) return

        val count = sliderPosition.toInt()
        val dispatcherName = when (dispatcherSelect) {
            R.string.dispatcher_io -> "IO"
            R.string.dispatcher_main -> "Main"
            else -> "Default"
        }
        val dispatcher = getSelectedDispatcher(dispatcherName)

        completedCount = 0
        totalCount = count
        isProcessing = true

        val scope = CoroutineScope(Dispatchers.Main)
        executionScope = scope

        scope.launch {
            println("TEST TAG: Запуск $count корутин")

            if (isLazy) {
                println("TEST TAG: Ленивый режим - ожидание 2 секунды...")
                delay(2000L)
                println("TEST TAG: Запускаем корутины после задержки")
            }

            if (isSequential) {
                println("TEST TAG: Последовательный запуск")
                for (i in 1..count) {
                    if (!coroutineContext.isActive) {
                        println("TEST TAG: Выполнение отменено")
                        break
                    }

                    try {
                        withContext(dispatcher) {
                            launchSingleCoroutine(i, context)
                        }
                        withContext(Dispatchers.Main) {
                            completedCount++
                        }
                    } catch (e: Exception) {
                        if (e is kotlinx.coroutines.CancellationException) {
                            println("TEST TAG: Корутина $i отменена")
                            throw e
                        }
                        handleException(e, i)
                        withContext(Dispatchers.Main) {
                            completedCount++
                        }
                    }
                }
            } else {
                println("TEST TAG: Параллельный запуск")
                val jobs = mutableListOf<Job>()

                for (i in 1..count) {
                    if (!coroutineContext.isActive) {
                        println("TEST TAG: Выполнение отменено перед созданием корутины $i")
                        break
                    }

                    val job = launch(dispatcher) {
                        try {
                            launchSingleCoroutine(i, context)
                        } catch (e: Exception) {
                            if (e !is kotlinx.coroutines.CancellationException) {
                                handleException(e, i)
                            }
                        } finally {
                            withContext(Dispatchers.Main) {
                                completedCount++
                            }
                        }
                    }
                    jobs.add(job)
                }

                jobs.forEach { it.join() }
            }

            println("TEST TAG: Все корутины завершены")

            withContext(Dispatchers.Main) {
                isProcessing = false
                executionScope = null
            }
        }
    }

    fun cancelCoroutines() {
        val scope = executionScope
        if (scope != null && isProcessing) {
            val cancelledCount = totalCount - completedCount

            scope.cancel(context.getString(R.string.scope_cancel))

            coroutineScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_coroutines_cancelled, cancelledCount, totalCount),
                    Toast.LENGTH_LONG
                ).show()
            }

            println("TEST TAG: Отменено $cancelledCount корутин")

            isProcessing = false
            executionScope = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.label_coroutines_count, sliderPosition.toInt()),
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )

                Slider(
                    value = sliderPosition,
                    onValueChange = { newValue ->
                        val roundedValue = ((newValue / 5).roundToInt() * 5)
                            .coerceIn(10, 100)
                            .toFloat()
                        sliderPosition = roundedValue
                    },
                    valueRange = 10f..100f,
                    steps = 18,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                )

                Text(
                    text = stringResource(R.string.label_dispatcher),
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )

                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { dropdownExpanded = true },
                        enabled = !isProcessing
                    ) {
                        Text(text = stringResource(dispatcherSelect))
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        listOf(
                            R.string.dispatcher_default,
                            R.string.dispatcher_io,
                            R.string.dispatcher_main
                        ).forEach { dispatcherResId ->
                            DropdownMenuItem(
                                text = { Text(stringResource(dispatcherResId)) },
                                onClick = {
                                    dispatcherSelect = dispatcherResId
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.label_launch_mode),
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )

                RowWithSwitch(
                    text = stringResource(R.string.label_sequential_launch),
                    checked = isSequential,
                    onCheckedChange = { newValue ->
                        isSequential = newValue
                        isParallel = !newValue
                    },
                    enabled = !isProcessing
                )

                RowWithSwitch(
                    text = stringResource(R.string.label_parallel_launch),
                    checked = isParallel,
                    onCheckedChange = { newValue ->
                        isParallel = newValue
                        isSequential = !newValue
                    },
                    enabled = !isProcessing
                )

                RowWithSwitch(
                    text = stringResource(R.string.label_lazy_launch),
                    checked = isLazy,
                    onCheckedChange = { isLazy = it },
                    enabled = !isProcessing
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isProcessing) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.label_progress_completed, completedCount, totalCount),
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )

                        if (isLazy && completedCount == 0) {
                            Text(
                                text = stringResource(R.string.label_waiting_launch),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                ) {
                    if (isProcessing) {
                        Button(
                            onClick = { cancelCoroutines() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.button_cancel_coroutines, completedCount, totalCount))
                        }
                    } else {
                        Button(
                            onClick = { startCoroutines() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isProcessing
                        ) {
                            Text(stringResource(R.string.button_start_coroutines, sliderPosition.toInt()))
                        }
                    }
                }

                if (isProcessing) {
                    Text(
                        text = when {
                            isLazy && completedCount == 0 -> stringResource(R.string.status_waiting_launch)
                            isSequential -> stringResource(R.string.status_sequential_execution)
                            else -> stringResource(R.string.status_parallel_execution)
                        },
                        modifier = Modifier.padding(8.dp),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                }

                LaunchedEffect(isProcessing) {
                    if (isProcessing) {
                        println("TEST TAG: Начало выполнения корутин")
                        println("TEST TAG: Параметры: count=$totalCount, " +
                                "sequential=$isSequential, lazy=$isLazy, ")
                    }
                }
            }
        }
    }
}

@Composable
fun RowWithSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}