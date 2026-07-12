package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var fontSize by remember { mutableStateOf(16.sp) }
            var isTibetan by remember { mutableStateOf(false) }
            var showAbout by remember { mutableStateOf(false) }
            var volume by remember { mutableStateOf(1.0f) }
            var scale by remember { mutableStateOf(1f) }
            var searchQuery by remember { mutableStateOf("") }
            
            var selectedWord by remember { mutableStateOf<String?>(null) }
            var selectedDefinition by remember { mutableStateOf<String?>(null) }
            var selectedEnglishDefinition by remember { mutableStateOf<String?>(null) }

            BackHandler(enabled = selectedWord != null) {
                selectedWord = null
                selectedDefinition = null
                selectedEnglishDefinition = null
            }

            CompositionLocalProvider(LocalFontSize provides fontSize, LocalIsTibetan provides isTibetan) {
                MyApplicationTheme(darkTheme = isDarkMode, dynamicColor = false) {
                    val pagerState = rememberPagerState(pageCount = { 3 })
                    val scope = rememberCoroutineScope()
                    val items = listOf("Search", "Word of Day", "Settings")
                    val icons = listOf(Icons.Default.Search, Icons.AutoMirrored.Filled.MenuBook, Icons.Default.Settings)
                    val tibetanItems = listOf("འཚོལ།", "ཉིན་རེའི་ཚིག", "སྒྲིག་བཀོད།")

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, _, zoom, _ ->
                                scale *= zoom
                                scale = scale.coerceIn(1f, 3f)
                            }
                        }
                    ) {
                        Scaffold(
                            bottomBar = {
                                NavigationBar {
                                    items.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = pagerState.currentPage == index,
                                            onClick = { 
                                                scope.launch { 
                                                    selectedWord = null
                                                    selectedDefinition = null
                                                    selectedEnglishDefinition = null
                                                    pagerState.animateScrollToPage(index) 
                                                } 
                                            },
                                            label = { Text(if (isTibetan) tibetanItems[index] else item, fontSize = LocalFontSize.current) },
                                            icon = { Icon(icons[index], contentDescription = item) }
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            HorizontalPager(state = pagerState, modifier = Modifier.padding(innerPadding)) { page ->
                                val context = LocalContext.current
                                val dictionaryDao = DependencyInjector.getDictionaryDatabase(context).dictionaryDao()
                                
                                when (page) {
                                    0 -> {
                                        val dao = DependencyInjector.getDatabase(context).searchHistoryDao()
                                        if (selectedWord == null || selectedDefinition == null) {
                                            DashboardScreen(
                                                dao = dao,
                                                dictionaryDao = dictionaryDao,
                                                volume = volume,
                                                searchQuery = searchQuery,
                                                onSearchQueryChange = { searchQuery = it },
                                                onWordClick = { word, definition, englishDef ->
                                                    selectedWord = word
                                                    selectedDefinition = definition
                                                    selectedEnglishDefinition = englishDef
                                                }
                                            )
                                        } else {
                                            DetailScreen(
                                                word = selectedWord!!,
                                                definition = selectedDefinition!!,
                                                englishDefinition = selectedEnglishDefinition,
                                                onBack = { 
                                                    selectedWord = null
                                                    selectedDefinition = null
                                                    selectedEnglishDefinition = null
                                                },
                                                onSearchQueryChange = { newQuery ->
                                                    searchQuery = newQuery
                                                },
                                                onNavigateToSearch = {
                                                    selectedWord = null
                                                    selectedDefinition = null
                                                    selectedEnglishDefinition = null
                                                }
                                            )
                                        }
                                    }
                                    1 -> {
                                        if (selectedWord == null || selectedDefinition == null) {
                                            WordsOfTheDayScreen(
                                                dictionaryDao = dictionaryDao,
                                                volume = volume,
                                                onWordClick = { word, definition, englishDef ->
                                                    selectedWord = word
                                                    selectedDefinition = definition
                                                    selectedEnglishDefinition = englishDef
                                                }
                                            )
                                        } else {
                                            DetailScreen(
                                                word = selectedWord!!,
                                                definition = selectedDefinition!!,
                                                englishDefinition = selectedEnglishDefinition,
                                                onBack = { 
                                                    selectedWord = null
                                                    selectedDefinition = null
                                                    selectedEnglishDefinition = null
                                                },
                                                onSearchQueryChange = { newQuery ->
                                                    searchQuery = newQuery
                                                },
                                                onNavigateToSearch = {
                                                    selectedWord = null
                                                    selectedDefinition = null
                                                    selectedEnglishDefinition = null
                                                }
                                            )
                                        }
                                    }
                                    2 -> SettingsScreen(
                                        isDarkMode = isDarkMode, 
                                        onDarkModeChange = { isDarkMode = it }, 
                                        onFontSizeChange = { fontSize = it },
                                        isTibetan = isTibetan,
                                        onLanguageChange = { isTibetan = it },
                                        onShowAbout = { showAbout = true },
                                        volume = volume,
                                        onVolumeChange = { volume = it }
                                    )
                                }
                            }
                        }
                    }
                    if (showAbout) {
                        AboutScreen(isTibetan = isTibetan, onDismiss = { showAbout = false })
                    }
                }
            }
        }
    }
}