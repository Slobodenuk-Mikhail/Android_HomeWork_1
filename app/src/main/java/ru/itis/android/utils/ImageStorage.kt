package ru.itis.android.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object ImageStorage {

    private const val IMAGE_DIRECTORY = "game_images"
    private const val MAX_IMAGE_SIZE_KB = 1024 // Максимальный размер изображения 1MB

    /**
     * Копирует изображение из URI в локальное хранилище приложения
     * @param context контекст приложения
     * @param uri исходный URI изображения
     * @return путь к сохраненному изображению в локальном хранилище
     */
    suspend fun saveImageToInternalStorage(
        context: Context,
        uri: Uri
    ): String = withContext(Dispatchers.IO) {
        return@withContext try {
            // 1. Создаем уникальное имя файла
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "GAME_IMG_${timeStamp}_${UUID.randomUUID().toString().take(8)}.jpg"

            // 2. Получаем или создаем директорию для изображений
            val storageDir = getOrCreateImageDirectory(context)

            // 3. Создаем файл
            val imageFile = File(storageDir, fileName)

            // 4. Оптимизируем и сохраняем изображение
            val optimizedBitmap = loadAndOptimizeBitmap(context.contentResolver, uri)

            if (optimizedBitmap != null) {
                // Сохраняем оптимизированный bitmap
                saveBitmapToFile(optimizedBitmap, imageFile, Bitmap.CompressFormat.JPEG, 80)
                optimizedBitmap.recycle()
            } else {
                // Если не удалось оптимизировать, просто копируем
                copyUriToFile(context.contentResolver, uri, imageFile)
            }

            // 5. Возвращаем путь к файлу
            "file://${imageFile.absolutePath}"

        } catch (e: Exception) {
            e.printStackTrace()
            // Если не удалось сохранить, возвращаем оригинальный URI
            uri.toString()
        }
    }

    /**
     * Загружает и оптимизирует Bitmap
     */
    private fun loadAndOptimizeBitmap(
        contentResolver: ContentResolver,
        uri: Uri,
        maxSizeKb: Int = MAX_IMAGE_SIZE_KB
    ): Bitmap? {
        return try {
            // Первый проход: получаем размеры изображения без загрузки в память
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }

            // Рассчитываем коэффициент уменьшения
            val (width, height) = options.outWidth to options.outHeight
            val scale = calculateInSampleSize(width, height, maxSizeKb)

            // Второй проход: загружаем с оптимизацией
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = scale
                inPreferredConfig = Bitmap.Config.RGB_565 // Экономим память
            }

            contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, decodeOptions)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Рассчитывает коэффициент уменьшения изображения
     */
    private fun calculateInSampleSize(
        width: Int,
        height: Int,
        maxSizeKb: Int
    ): Int {
        var inSampleSize = 1

        // Рассчитываем текущий размер в KB (примерно)
        var currentSizeKb = (width * height * 2) / 1024 // 2 байта на пиксель для RGB_565

        while (currentSizeKb > maxSizeKb && inSampleSize < 32) {
            inSampleSize *= 2
            currentSizeKb /= 4 // Уменьшаем в 4 раза при каждом увеличении inSampleSize
        }

        return inSampleSize
    }

    /**
     * Сохраняет Bitmap в файл
     */
    private fun saveBitmapToFile(
        bitmap: Bitmap,
        file: File,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 80
    ) {
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(format, quality, outputStream)
            outputStream.flush()
        }
    }

    /**
     * Копирует данные из URI в файл (без оптимизации)
     */
    private fun copyUriToFile(
        contentResolver: ContentResolver,
        uri: Uri,
        file: File
    ) {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    /**
     * Получает или создает директорию для изображений
     */
    private fun getOrCreateImageDirectory(context: Context): File {
        // Пробуем получить внешнее хранилище
        val externalDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val internalDir = File(context.filesDir, IMAGE_DIRECTORY)

        val targetDir = externalDir ?: internalDir

        // Создаем директорию если не существует
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        return targetDir
    }

    /**
     * Получает URI для отображения (конвертирует если нужно)
     */
    fun getDisplayUri(uriString: String): String {
        return if (uriString.startsWith("content://")) {
            // Если это content URI, возвращаем как есть
            uriString
        } else if (uriString.startsWith("/")) {
            // Если это путь без префикса
            "file://$uriString"
        } else if (!uriString.startsWith("file://")) {
            // Если нет префикса file://
            "file://$uriString"
        } else {
            uriString
        }
    }

    /**
     * Проверяет доступность изображения по URI
     */
    suspend fun isImageAvailable(context: Context, uriString: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val uri = Uri.parse(getDisplayUri(uriString))
                val file = File(uri.path ?: return@withContext false)

                if (file.exists() && file.length() > 0) {
                    true
                } else {
                    // Пробуем через ContentResolver
                    context.contentResolver.openInputStream(uri)?.use {
                        it.available() > 0
                    } ?: false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Получает размер файла изображения
     */
    suspend fun getImageFileSize(context: Context, uriString: String): Long {
        return withContext(Dispatchers.IO) {
            try {
                val uri = Uri.parse(getDisplayUri(uriString))
                val file = File(uri.path ?: return@withContext 0L)

                if (file.exists()) {
                    file.length()
                } else {
                    0L
                }
            } catch (e: Exception) {
                0L
            }
        }
    }

    /**
     * Очищает старые изображения
     * @param context контекст приложения
     * @param daysToKeep сколько дней хранить изображения (по умолчанию 30)
     * @return количество удаленных файлов
     */
    suspend fun cleanupOldImages(context: Context, daysToKeep: Int = 30): Int {
        return withContext(Dispatchers.IO) {
            try {
                val directory = getOrCreateImageDirectory(context)
                val files = directory.listFiles() ?: emptyArray()

                val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
                var deletedCount = 0

                files.forEach { file ->
                    if (file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            deletedCount++
                        }
                    }
                }

                deletedCount
            } catch (e: Exception) {
                0
            }
        }
    }

    /**
     * Очищает все изображения в хранилище
     * @return true если очистка прошла успешно
     */
    suspend fun clearAllImages(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val directory = getOrCreateImageDirectory(context)
                val files = directory.listFiles() ?: emptyArray()

                var allDeleted = true
                files.forEach { file ->
                    if (!file.delete()) {
                        allDeleted = false
                    }
                }

                allDeleted
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Получает общий размер всех изображений в хранилище
     */
    suspend fun getTotalStorageSize(context: Context): Long {
        return withContext(Dispatchers.IO) {
            try {
                val directory = getOrCreateImageDirectory(context)
                val files = directory.listFiles() ?: emptyArray()

                files.sumOf { it.length() }
            } catch (e: Exception) {
                0L
            }
        }
    }

    /**
     * Удаляет конкретное изображение
     * @param uriString URI изображения для удаления
     * @return true если удаление прошло успешно
     */
    suspend fun deleteImage(context: Context, uriString: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val uri = Uri.parse(getDisplayUri(uriString))
                val file = File(uri.path ?: return@withContext false)

                if (file.exists()) {
                    file.delete()
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Копирует файл изображения в другое место
     * @param sourceUriString исходный URI
     * @param destinationFile целевой файл
     * @return true если копирование успешно
     */
    suspend fun copyImageToFile(
        context: Context,
        sourceUriString: String,
        destinationFile: File
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sourceUri = Uri.parse(getDisplayUri(sourceUriString))

                context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: false

                destinationFile.exists()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Создает миниатюру изображения
     * @param maxWidth максимальная ширина миниатюры
     * @param maxHeight максимальная высота миниатюры
     * @return путь к файлу миниатюры или null
     */
    suspend fun createThumbnail(
        context: Context,
        sourceUriString: String,
        maxWidth: Int = 200,
        maxHeight: Int = 200
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val sourceUri = Uri.parse(getDisplayUri(sourceUriString))

                // Загружаем изображение
                val bitmap = loadAndOptimizeBitmap(context.contentResolver, sourceUri, 512) ?: return@withContext null

                // Создаем миниатюру
                val thumbnail = if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
                    val ratio = minOf(
                        maxWidth.toFloat() / bitmap.width,
                        maxHeight.toFloat() / bitmap.height
                    )
                    val newWidth = (bitmap.width * ratio).toInt()
                    val newHeight = (bitmap.height * ratio).toInt()

                    Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
                } else {
                    bitmap
                }

                // Сохраняем миниатюру
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "THUMB_${timeStamp}_${UUID.randomUUID().toString().take(8)}.jpg"
                val storageDir = getOrCreateImageDirectory(context)
                val thumbnailFile = File(storageDir, fileName)

                saveBitmapToFile(thumbnail, thumbnailFile, Bitmap.CompressFormat.JPEG, 70)

                // Освобождаем память
                bitmap.recycle()
                if (thumbnail != bitmap) {
                    thumbnail.recycle()
                }

                "file://${thumbnailFile.absolutePath}"
            } catch (e: Exception) {
                null
            }
        }
    }
}