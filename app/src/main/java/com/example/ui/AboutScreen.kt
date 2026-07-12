package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

// Premium Tibetan-Art Palette Color Tokens
private val ParchmentBg = Color(0xFFFDFBF7)
private val TibetanMaroon = Color(0xFF8C1D40)
private val SaffronGold = Color(0xFFD99B26)
private val DeepSlateText = Color(0xFF2C2C2C)
private val MutedDivider = Color(0xFFEFEBE4)
private val LinkBlue = Color(0xFF1877F2)

@Composable
fun AboutScreen(isTibetan: Boolean, onDismiss: () -> Unit) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ParchmentBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TibetanMaroon
                    )
                }
                Text(
                    text = if (isTibetan) "སྐོར།" else "About",
                    style = MaterialTheme.typography.titleMedium,
                    color = TibetanMaroon,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Profile Picture Frame (Gold Ring Border)
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, SaffronGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profilepic), 
                    contentDescription = "Developer Profile",
                    modifier = Modifier
                        .size(102.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Creator Tag
            Text(
                text = if (isTibetan) "བསྟན་འཛིན་དབང་འདུས།" else "Created by Tenzin",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TibetanMaroon
            )
            Text(
                text = "@tenzinlama212",
                style = MaterialTheme.typography.bodyMedium,
                color = SaffronGold,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = MutedDivider, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(24.dp))

            // 4. Main Content Card (Dictionary Intro)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MutedDivider)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isTibetan) {
                            "ཚིག་མཛོད་འདི་ནི་དབྱིན་བོད་དང་། བོད་དབྱིན། བོད་-བོད་བཅས་ཕྱོགས་མང་ཤན་སྦྱར་གྱི་མཛོད་ཆེན་ཞིག་ཡིན། འདིའི་གཞི་གྲངས་ནི་རྒྱ་ཆེའི་མཉམ་སྤྱོད་ལག་རྩལ་ཚོགས་པ་ནས་བྱུང་བ་ཡིན།"
                        } else {
                            "Welcome to the Great Tibetan Dictionary—a comprehensive, multi-directional reference engine supporting English-to-Tibetan, Tibetan-to-English, and Tibetan-to-Tibetan translations. This resource is proudly built upon open-source database records compiled by community contributors."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepSlateText,
                        lineHeight = if (isTibetan) 28.sp else 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isTibetan) {
                            "Welcome to the Great Tibetan Dictionary—a comprehensive, multi-directional reference engine supporting English-to-Tibetan, Tibetan-to-English, and Tibetan-to-Tibetan translations. This resource is proudly built upon open-source database records compiled by community contributors."
                        } else {
                            "Language is a living heritage. If you discover any missing definitions, spelling typos, or formatting errors, your corrections are deeply valued to help improve this resource for everyone. Reach me through email or other social handle provided below. Thank You!"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        lineHeight = if (isTibetan) 26.sp else 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Heartfelt Tribute to Dad (Interactive Layout Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SaffronGold.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isTibetan) "བཀའ་དྲིན་རྗེས་དྲན།" else "DEDICATION & INSPIRATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = SaffronGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Cross-language father dedication
                    Text(
                        text = if (isTibetan) {
                            "Deepest gratitude to my DAD, who has always inspired me to preserve, honor, and keep the sacred Tibetan language alive for future generations."

                        } else {
                            "Deepest gratitude to my DAD, who has always inspired me to preserve, honor, and keep the sacred Tibetan language alive for future generations."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepSlateText,
                        lineHeight = 22.sp,
                        fontStyle = FontStyle.Italic
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // Dad's clickable Facebook link block
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/dorje.mingyur.2025"))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🌐 ", fontSize = 14.sp)
                        Text(
                            text = "Connect with Dorje Mingyur on Facebook",
                            style = MaterialTheme.typography.bodySmall,
                            color = LinkBlue,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Humble Nepali Quote (For Local Accessibility & Hobby Note)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MutedDivider)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "यो यो एपको पहिलो संस्करण (Version 1.0) हो, जुन मैले केवल आफ्नो रुचि र सोख (Hobby) को लागि विकास गरेको हुँ। अहिले यसमा सामान्य त्रुटिहरू हुन सक्छन्, म भविष्यमा यसलाई अझै परिमार्जन गर्दै नयाँ सुविधाहरू थप्नेछु र निरन्तर काम गरिरहनेछु।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepSlateText,
                        textAlign = TextAlign.Start,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 7. Real Custom Social Links
            Text(
                text = "CONNECT WITH ME",
                style = MaterialTheme.typography.labelMedium,
                color = SaffronGold,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Configured with your real URLs 
                val socialChannels = listOf(
                    "Instagram" to "https://www.instagram.com/tenzinlama212/",
                    "GitHub" to "https://github.com/tenzinl4ma",
                    "Facebook" to "https://www.facebook.com/terodaitenwang"
                )

                socialChannels.forEach { (name, url) ->
                    SuggestionChip(
                        onClick = {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(browserIntent)
                        },
                        label = { Text(name, color = TibetanMaroon, fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(20.dp),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = MutedDivider
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 8. Primary Action Feedback Button
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:tenzinlama212@gmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "Feedback for Tibetan Dictionary App")
                        putExtra(Intent.EXTRA_TEXT, "Hello Tenzin,\n\nI want to suggest the following correction:\nTibetan Word:\nDefinition change:")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TibetanMaroon),
                shape = RoundedCornerShape(27.dp)
            ) {
                Text(
                    text = if (isTibetan) "འབྲེལ་བ་གནང་རྒྱུ།" else "Send Email Feedback",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}