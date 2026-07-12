package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.DictionaryDao
import com.example.data.entity.EnToTb
import com.example.data.entity.TbToEn
import com.example.data.entity.TbToTb

@Database(entities = [EnToTb::class, TbToEn::class, TbToTb::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao

    companion object {
        const val DATABASE_NAME = "MLDic.db"
    }
}
