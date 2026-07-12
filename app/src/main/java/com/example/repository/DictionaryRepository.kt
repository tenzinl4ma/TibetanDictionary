package com.example.repository

import com.example.data.dao.DictionaryDao
import kotlinx.coroutines.flow.Flow
import com.example.data.entity.EnToTb
import com.example.data.entity.TbToEn
import com.example.data.entity.TbToTb

class DictionaryRepository(private val dao: DictionaryDao) {
    fun searchEnToTb(query: String) = dao.searchEnToTb(query)
    fun searchTbToEn(query: String) = dao.searchTbToEn(query)
    fun searchTbToTb(query: String) = dao.searchTbToTb(query)
}
