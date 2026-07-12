package com.example.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.SearchHistoryDao
import com.example.data.dao.DictionaryDao
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    dao: SearchHistoryDao, 
    dictionaryDao: DictionaryDao, 
    volume: Float, 
    searchQuery: String, 
    onSearchQueryChange: (String) -> Unit,
    onWordClick: (String, String, String?) -> Unit
) {
    val viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(dao))
    val recentSearches by viewModel.recentSearches.collectAsState()
    val fontSize = LocalFontSize.current
    val isTibetan = LocalIsTibetan.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set language if needed
            }
        }
    }
    
    val tibetanResults = remember { mutableStateListOf<Pair<String?, String?>>() }
    val englishResults = remember { mutableStateListOf<Pair<String?, String?>>() }

    LaunchedEffect(searchQuery, dictionaryDao) {
        tibetanResults.clear()
        englishResults.clear()
        if (searchQuery.isBlank()) {
            return@LaunchedEffect
        }

        try {
            val containsTibetan = searchQuery.any { it in '\u0F00'..'\u0FFF' }
            coroutineScope {
                if (containsTibetan) {
                    launch(Dispatchers.IO) {
                        dictionaryDao.searchTbToTb(searchQuery).collectLatest { entries ->
                            tibetanResults.clear()
                            tibetanResults.addAll(entries.map { it.word to it.definition })
                        }
                    }
                    launch(Dispatchers.IO) {
                        dictionaryDao.searchTbToEn(searchQuery).collectLatest { entries ->
                            englishResults.clear()
                            englishResults.addAll(entries.map { it.word to it.definition })
                        }
                    }
                } else {
                    launch(Dispatchers.IO) {
                        dictionaryDao.searchEnToTb(searchQuery).collectLatest { entries ->
                            tibetanResults.clear()
                            tibetanResults.addAll(entries.map { it.word to it.definition })
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("DashboardScreen", "search error", e)
            tibetanResults.clear()
            englishResults.clear()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)
        .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {
        Text(
            "༄༅། ཚིག་མཛོད་ཆེན་པོ།", 
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSize), 
            color = MaterialTheme.colorScheme.onBackground, 
            fontWeight = FontWeight.Bold
        )
        Text(
            "The Great Dictionary", 
            style = MaterialTheme.typography.labelSmall.copy(fontSize = fontSize * 0.7f), 
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(if (isTibetan) "ཚིག་འཚོལ།" else "Search words...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { 
                focusManager.clearFocus()
                viewModel.addSearch(searchQuery)
            }),
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { 
                        focusManager.clearFocus()
                        viewModel.addSearch(searchQuery)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            } else null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (searchQuery.isNotEmpty()) {
                if (tibetanResults.isEmpty() && englishResults.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isTibetan) "ཚིག་མི་འདུག" else "Word not found",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isTibetan) "གནས་ཚུལ་འདི་གཞི་གྲངས་ཁྲིམས་སུ་མ་རྙེད།" else "No matching definition found in the database.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    if (tibetanResults.isNotEmpty()) {
                        item {
                            Text(
                                text = if (isTibetan) "བོད་ཡིག་གི་ཚིག་འགྲེལ།" else "Tibetan Definitions",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(tibetanResults.size) { index ->
                            val (word, definition) = tibetanResults[index]
                            ResultCardItem(
                                word = word,
                                definition = definition,
                                fontSize = fontSize,
                                volume = volume,
                                tts = tts,
                                onWordClick = { w, d ->
                                    val matchingEng = englishResults.firstOrNull { it.first == w }?.second
                                    onWordClick(w, d, matchingEng)
                                }
                            )
                        }
                    }

                    if (englishResults.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isTibetan) "དབྱིན་ཡིག་གི་ཚིག་འགྲེལ།" else "English Definitions",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(englishResults.size) { index ->
                            val (word, definition) = englishResults[index]
                            ResultCardItem(
                                word = word,
                                definition = definition,
                                fontSize = fontSize,
                                volume = volume,
                                tts = tts,
                                onWordClick = { w, d ->
                                    onWordClick(w, "", d)
                                }
                            )
                        }
                    }
                }
            } else {
                item { Text(if (isTibetan) "ཉེ་བའི་འཚོལ་བ།" else "Recent Searches", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground) }
                items(recentSearches.size) { index ->
                    Text(
                        text = recentSearches[index].query, 
                        style = MaterialTheme.typography.bodyMedium, 
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSearchQueryChange(recentSearches[index].query) }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultCardItem(
    word: String?,
    definition: String?,
    fontSize: androidx.compose.ui.unit.TextUnit,
    volume: Float,
    tts: TextToSpeech,
    onWordClick: (String, String) -> Unit
) {
    val hasTibetan = (word ?: "").any { it in '\u0F00'..'\u0FFF' }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)), RoundedCornerShape(12.dp))
            .clickable { onWordClick(word ?: "", definition ?: "") }
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(word ?: "", style = MaterialTheme.typography.bodyLarge.copy(fontSize = fontSize, fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
                if (!definition.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(definition, style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSize * 0.85f), color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                }
            }
            if (!hasTibetan) {
                IconButton(onClick = { 
                    val textToSpeak = (word ?: "").trim()
                    if (textToSpeak.isNotEmpty()) {
                        val params = Bundle()
                        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume)
                        tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, null)
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Pronounce", tint = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}