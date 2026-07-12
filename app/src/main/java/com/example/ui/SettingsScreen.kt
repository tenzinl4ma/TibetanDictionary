package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LocalFontSize

@Composable
fun SettingsScreen(
    isDarkMode: Boolean, 
    onDarkModeChange: (Boolean) -> Unit, 
    onFontSizeChange: (TextUnit) -> Unit,
    isTibetan: Boolean,
    onLanguageChange: (Boolean) -> Unit,
    onShowAbout: () -> Unit,
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    val fontSize = LocalFontSize.current

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (isTibetan) "སྒྲིག་བཀོད།" else "Accessibility Settings", style = MaterialTheme.typography.titleLarge.copy(fontSize = fontSize), color = MaterialTheme.colorScheme.onBackground)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (isTibetan) "དཀར་ནག་རྣམ་པ།" else "Dark Mode", color = MaterialTheme.colorScheme.onBackground, fontSize = fontSize)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (isTibetan) "སྐད་ཡིག: བོད་ཡིག" else "Language: English", color = MaterialTheme.colorScheme.onBackground, fontSize = fontSize)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isTibetan, onCheckedChange = onLanguageChange)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(if (isTibetan) "ཡིག་གཟུགས་ཆེ་ཆུང་།: ${fontSize.value.toInt()}" else "Font Size: ${fontSize.value.toInt()}", color = MaterialTheme.colorScheme.onBackground, fontSize = fontSize)
        Slider(value = fontSize.value, onValueChange = { onFontSizeChange(it.sp) }, valueRange = 12f..21f)
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(if (isTibetan) "སྒྲ་སྐད།: ${(volume * 100).toInt()}%" else "Volume: ${(volume * 100).toInt()}%", color = MaterialTheme.colorScheme.onBackground, fontSize = fontSize)
        Slider(value = volume, onValueChange = onVolumeChange, valueRange = 0f..1f)
        
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onShowAbout, modifier = Modifier.fillMaxWidth()) {
            Text(if (isTibetan) "ངའི་སྐོར།" else "About Me", fontSize = fontSize)
        }
    }
}
