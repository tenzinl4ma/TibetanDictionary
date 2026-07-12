package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entotb")
data class EnToTb(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int? = null,
    @ColumnInfo(name = "word") val word: String? = null,
    @ColumnInfo(name = "definition") val definition: String? = null
)

@Entity(tableName = "tben")
data class TbToEn(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int? = null,
    @ColumnInfo(name = "word") val word: String? = null,
    @ColumnInfo(name = "definition") val definition: String? = null
)

@Entity(tableName = "tbtb")
data class TbToTb(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int? = null,
    @ColumnInfo(name = "word") val word: String? = null,
    @ColumnInfo(name = "definition") val definition: String? = null
)
