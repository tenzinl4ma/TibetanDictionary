package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repository.DictionaryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

enum class SearchMode { EN_TO_TB, TB_TO_EN, TB_TO_TB }

class DictionaryViewModel(private val repository: DictionaryRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchMode = MutableStateFlow(SearchMode.EN_TO_TB)
    val searchMode = _searchMode.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Any>> = _searchQuery
        .combine(_searchMode) { query, mode ->
            Pair(query, mode)
        }
        .flatMapLatest { (query, mode) ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                when (mode) {
                    // Make sure these function names match what is in your DictionaryRepository.kt
                    SearchMode.EN_TO_TB -> repository.searchEnToTb(query)
                    SearchMode.TB_TO_EN -> repository.searchTbToEn(query)
                    SearchMode.TB_TO_TB -> repository.searchTbToTb(query)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateQuery(query: String) { _searchQuery.value = query }
    fun updateMode(mode: SearchMode) { _searchMode.value = mode }

// 1. State to hold the word currently being viewed in the DetailScreen
private val _selectedWord = MutableStateFlow<Any?>(null) 
val selectedWord = _selectedWord.asStateFlow()

// 2. Function to open the Detail screen
fun selectWord(word: Any?) {
    _selectedWord.value = word
}

// 3. The "Deep Search" function for when a user long-presses a word
fun deepSearchFromDetail(wordToSearch: String) {
    // Clean up the word (remove spaces or punctuation if needed)
    val cleanWord = wordToSearch.trim()
    
    if (cleanWord.isNotEmpty()) {
        updateQuery(cleanWord)   // Put the new word into the search bar
        _selectedWord.value = null // Close the Detail screen to show results
    }
}
}