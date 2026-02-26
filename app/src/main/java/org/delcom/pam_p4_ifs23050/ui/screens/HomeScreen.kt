package org.delcom.pam_p4_ifs23050.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23050.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23050.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23050.ui.theme.DelcomTheme

// ─────────────────────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBarComponent(navController = navController, title = "Beranda", showBackButton = false)
        Box(modifier = Modifier.weight(1f)) { HomeUI() }
        BottomNavComponent(navController = navController)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UI
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        // ── Hero banner ──────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0D0820),
                            Color(0xFF1A0050),
                            Color(0xFF0A1A4E),
                        ),
                    )
                )
                .padding(vertical = 32.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✨", fontSize = 52.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    text  = "Delcom Zodiac",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color      = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text  = "Rasi Bintang · Zodiak · Konstelasi",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color      = Color(0xFFFFD600).copy(alpha = 0.9f),
                        fontStyle  = FontStyle.Italic,
                        letterSpacing = 0.8.sp,
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }

        // ── Penjelasan Zodiak ─────────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(18.dp),
            colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(3.dp),
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📖", fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text  = "Apa itu Zodiak?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color      = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text  = "Zodiak adalah 12 rasi bintang yang dilintasi matahari dalam setahun. Setiap zodiak memiliki sifat, elemen, dan makna unik yang dipercaya memengaruhi kepribadian seseorang berdasarkan tanggal lahir mereka.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp,
                    ),
                )
            }
        }

        // ── 12 Zodiak ────────────────────────────────────────────────────────
        Text(
            text  = "⭐ 12 Rasi Bintang Zodiak",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary,
            ),
        )

        val zodiacs = listOf(
            ZodiacCard("♈", "Aries",       "21 Mar – 19 Apr",  Color(0xFFEDE7F6)),
            ZodiacCard("♉", "Taurus",      "20 Apr – 20 Mei",  Color(0xFFE8EAF6)),
            ZodiacCard("♊", "Gemini",      "21 Mei – 20 Jun",  Color(0xFFF3E5F5)),
            ZodiacCard("♋", "Cancer",      "21 Jun – 22 Jul",  Color(0xFFE8EAF6)),
            ZodiacCard("♌", "Leo",         "23 Jul – 22 Agt",  Color(0xFFFFF9C4)),
            ZodiacCard("♍", "Virgo",       "23 Agt – 22 Sep",  Color(0xFFEDE7F6)),
            ZodiacCard("♎", "Libra",       "23 Sep – 22 Okt",  Color(0xFFE8EAF6)),
            ZodiacCard("♏", "Scorpio",     "23 Okt – 21 Nov",  Color(0xFFF3E5F5)),
            ZodiacCard("♐", "Sagitarius",  "22 Nov – 21 Des",  Color(0xFFFFF9C4)),
            ZodiacCard("♑", "Capricorn",   "22 Des – 19 Jan",  Color(0xFFEDE7F6)),
            ZodiacCard("♒", "Aquarius",    "20 Jan – 18 Feb",  Color(0xFFE8EAF6)),
            ZodiacCard("♓", "Pisces",      "19 Feb – 20 Mar",  Color(0xFFF3E5F5)),
        )

        zodiacs.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                pair.forEach { z ->
                    ZodiacMiniCard(z, modifier = Modifier.weight(1f))
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        // ── Elemen Zodiak ─────────────────────────────────────────────────────
        Text(
            text  = "🔥 Empat Elemen Zodiak",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary,
            ),
        )

        val elements = listOf(
            Triple("🔥", "Api (Aries, Leo, Sagitarius)",    "Bersemangat, penuh gairah, dan berani"),
            Triple("🌍", "Bumi (Taurus, Virgo, Capricorn)", "Stabil, praktis, dan dapat diandalkan"),
            Triple("💨", "Udara (Gemini, Libra, Aquarius)", "Intelektual, komunikatif, dan bebas"),
            Triple("💧", "Air (Cancer, Scorpio, Pisces)",   "Intuitif, emosional, dan penuh empati"),
        )

        elements.forEach { (emoji, name, desc) ->
            ElementCard(emoji = emoji, name = name, desc = desc)
        }

        // ── Kutipan ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1A0050),
                            Color(0xFF0A1A4E),
                        )
                    )
                )
                .padding(20.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text  = "✨",
                    fontSize = 36.sp,
                )
                Text(
                    text  = "Bintang-bintang di langit bukan sekadar cahaya — mereka adalah cerita yang menunggu untuk dibaca.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        color     = Color.White,
                        textAlign = TextAlign.Center,
                    ),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = "— Delcom Zodiac",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color      = Color(0xFFFFD600),
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Helpers
// ─────────────────────────────────────────────────────────────────────────────
data class ZodiacCard(val symbol: String, val name: String, val dates: String, val bg: Color)

@Composable
fun ZodiacMiniCard(z: ZodiacCard, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = z.bg),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier             = Modifier.padding(14.dp),
            horizontalAlignment  = Alignment.CenterHorizontally,
        ) {
            Text(z.symbol, fontSize = 32.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text  = z.name,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = z.dates,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontStyle = FontStyle.Italic,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ElementCard(emoji: String, name: String, desc: String) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text  = name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.primary,
                    ),
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text  = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun PreviewHomeUI() {
    DelcomTheme { HomeUI() }
}