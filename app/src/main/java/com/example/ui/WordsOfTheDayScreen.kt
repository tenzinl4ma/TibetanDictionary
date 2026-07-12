package com.example.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.dao.DictionaryDao
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlin.random.Random

data class WordOfTheDayItem(
    val word: String,
    val tibetanDefinition: String,
    val englishDefinition: String? = null
)

@Composable
fun WordsOfTheDayScreen(
    dictionaryDao: DictionaryDao,
    volume: Float,
    onWordClick: (String, String, String?) -> Unit
) {
    val fontSize = LocalFontSize.current
    val isTibetan = LocalIsTibetan.current

    var wordDeck by remember { mutableStateOf<List<WordOfTheDayItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(dictionaryDao) {
        isLoading = true
        try {
            val fetchedWords = withContext(Dispatchers.IO) {
                val tbList = dictionaryDao.searchTbToTb("ཀ").firstOrNull() ?: emptyList()
                val enList = dictionaryDao.searchTbToEn("ཀ").firstOrNull() ?: emptyList()
                
                // Seed the random with today's epoch day so it stays identical all day, changing only tomorrow
                val epochDay = System.currentTimeMillis() / 86400000L
                
                tbList.shuffled(Random(epochDay)).take(5).map { entry ->
                    val wordStr = entry.word ?: ""
                    WordOfTheDayItem(
                        word = wordStr,
                        tibetanDefinition = entry.definition ?: "",
                        englishDefinition = enList.firstOrNull { it.word == wordStr }?.definition
                    )
                }
            }
            wordDeck = fetchedWords
        } catch (e: Exception) {
            Log.e("WordsOfTheDayScreen", "Error loading word deck", e)
        } finally {
            isLoading = false
        }
    }

    val pagerState = rememberPagerState(pageCount = { if (wordDeck.isNotEmpty()) wordDeck.size else 1 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isTibetan) "ཉིན་རེའི་ཚིག་གསུམ།" else "Tibetan Word Deck",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSize * 1.2f),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isTibetan) "གོང་འོག་ཏུ་འདྲུད་དེ་གཟིགས།" else "Swipe up or down to explore daily words",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else if (wordDeck.isEmpty()) {
            Text(
                text = if (isTibetan) "ཚིག་མི་འདུག" else "No words available",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) { page ->
                val item = wordDeck[page]
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            onWordClick(item.word, item.tibetanDefinition, item.englishDefinition)
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = item.word,
                                style = MaterialTheme.typography.headlineSmall.copy(fontSize = fontSize * 1.3f),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = item.tibetanDefinition,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = fontSize),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 6
                            )
                        }

                        if (!item.englishDefinition.isNullOrBlank()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                            Text(
                                text = "Eng: ${item.englishDefinition}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSize * 0.85f),
                                color = MaterialTheme.colorScheme.tertiary,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}