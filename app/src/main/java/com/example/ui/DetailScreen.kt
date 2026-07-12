package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    word: String, 
    definition: String, 
    englishDefinition: String? = null, // UPDATED: Accepts optional English deck text
    onBack: () -> Unit, 
    onSearchQueryChange: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            TextButton(onClick = onBack) { 
                Text("← Back", color = MaterialTheme.colorScheme.primary) 
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tibetan Definition Card Deck
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = word, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val formattedDefinition = definition
                        .replace("\n", " \n ")
                        .replace("།", "། ")
                    
                    val words = formattedDefinition.split(Regex("\\s+")).filter { it.isNotEmpty() }
                    
                    FlowRow {
                        words.forEach { w ->
                            val cleanWord = w.replace(Regex("[^\\p{L}\\u0F00-\\u0FFF]"), "").trim('་')
                            var isPressed by remember { mutableStateOf(false) }
                            
                            Text(
                                text = "$w ",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .background(if (isPressed) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) else Color.Transparent)
                                    .pointerInput(cleanWord) {
                                        detectTapGestures(
                                            onPress = {
                                                isPressed = true
                                                tryAwaitRelease()
                                                isPressed = false
                                            },
                                            onLongPress = {
                                                if (cleanWord.isNotEmpty()) {
                                                    Toast.makeText(context, "Searching: $cleanWord", Toast.LENGTH_SHORT).show()
                                                    onSearchQueryChange(cleanWord)
                                                    onNavigateToSearch()
                                                }
                                            }
                                        )
                                    }
                            )
                        }
                    }
                }
            }

            // English Translation Deck (Only shows if englishDefinition exists)
            if (!englishDefinition.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "English Translation",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = englishDefinition,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}