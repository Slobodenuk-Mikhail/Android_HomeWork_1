package ru.itis.android.db.typeconverter

import androidx.room.TypeConverter
import java.util.Date

class InceptionConverters {

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromLongToDate(timeInMillis: Long): Date {
        return Date(timeInMillis)
    }
}