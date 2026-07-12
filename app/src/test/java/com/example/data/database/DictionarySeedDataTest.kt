package com.example.data.database

import org.junit.Assert.assertTrue
import org.junit.Test

class DictionarySeedDataTest {
    @Test
    fun seedData_containsTibetanAndEnglishEntries() {
        val entries = DictionarySeedData.entries()
        val tbToEnEntries = DictionarySeedData.tbToEnEntries()

        assertTrue(entries.isNotEmpty())
        assertTrue(entries.any { it.word.contains("Compassion", ignoreCase = true) })
        assertTrue(tbToEnEntries.any { it.word.contains("བྱམས་པ།", ignoreCase = true) })
    }
}
