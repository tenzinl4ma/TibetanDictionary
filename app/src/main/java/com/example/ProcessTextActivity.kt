package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.ui.theme.MyApplicationTheme

class ProcessTextActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = intent.getCharSequenceExtra(android.content.Intent.EXTRA_PROCESS_TEXT)?.toString() ?: ""
        
        setContent {
            MyApplicationTheme {
                val sheetState = rememberModalBottomSheetState()
                var showBottomSheet by remember { mutableStateOf(true) }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                            finish()
                        },
                        sheetState = sheetState
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            Text(text = "Definition for: $text", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Placeholder definition for Tibetan word.")
                        }
                    }
                }
            }
        }
    }
}
