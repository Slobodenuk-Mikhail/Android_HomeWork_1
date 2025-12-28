package ru.itis.android.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

     val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Создаем таблицу games
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS games (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title TEXT NOT NULL,
                    genre TEXT NOT NULL,
                    rating INTEGER NOT NULL,
                    date_of_create TEXT NOT NULL,
                    author_id INTEGER NOT NULL,
                    image_url TEXT NOT NULL DEFAULT '',
                    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """)
        }
    }
}