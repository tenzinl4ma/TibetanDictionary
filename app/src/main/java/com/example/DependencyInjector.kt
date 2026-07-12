package com.example

import android.content.Context
import androidx.room.Room
import com.example.data.HistoryDatabase
import com.example.data.database.AppDatabase
import java.io.File
import java.io.FileOutputStream

object DependencyInjector {
    private var historyDb: HistoryDatabase? = null
    private var dictionaryDb: AppDatabase? = null

    fun getDatabase(context: Context): HistoryDatabase {
        return historyDb ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                HistoryDatabase::class.java,
                "history_database"
            ).build().also { historyDb = it }
        }
    }

    fun getDictionaryDatabase(context: Context): AppDatabase {
        return dictionaryDb ?: synchronized(this) {
            val dbFile = File(context.getDatabasePath(AppDatabase.DATABASE_NAME).path)
            val assetPath = "databases/MLDic.ml"

            if (dbFile.exists()) {
                dbFile.delete()
            }

            context.assets.open(assetPath).use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }

            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build().also { dictionaryDb = it }
        }
    }
}
