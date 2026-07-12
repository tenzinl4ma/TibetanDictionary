package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.EnToTb
import com.example.data.entity.TbToEn
import com.example.data.entity.TbToTb
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Query("""
        SELECT * FROM entotb 
        WHERE word LIKE '%' || :query || '%' 
        ORDER BY 
            CASE 
                WHEN word = :query THEN 1 
                WHEN word LIKE :query || '%' THEN 2 
                ELSE 3 
            END, 
            word ASC
    """)
    fun searchEnToTb(query: String): Flow<List<EnToTb>>

    @Query("""
        SELECT * FROM tben 
        WHERE word LIKE '%' || :query || '%' 
        ORDER BY 
            CASE 
                WHEN word = :query THEN 1 
                WHEN word LIKE :query || '%' THEN 2 
                ELSE 3 
            END, 
            word ASC
    """)
    fun searchTbToEn(query: String): Flow<List<TbToEn>>

    @Query("""
        SELECT * FROM tbtb 
        WHERE word LIKE '%' || :query || '%' 
        ORDER BY 
            CASE 
                WHEN word = :query THEN 1 
                WHEN word LIKE :query || '%' THEN 2 
                ELSE 3 
            END, 
            word ASC
    """)
    fun searchTbToTb(query: String): Flow<List<TbToTb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnToTb(entries: List<EnToTb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTbToEn(entries: List<TbToEn>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTbToTb(entries: List<TbToTb>)
}