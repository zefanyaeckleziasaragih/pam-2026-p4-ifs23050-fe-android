package org.delcom.pam_p4_ifs23051.ui.screens

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
import org.delcom.pam_p4_ifs23051.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23051.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23051.ui.theme.DelcomTheme

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
                        colors = listOf(Color(0xFFC2185B), Color(0xFF7B1FA2), Color(0xFFAD1457)),
                    )
                )
                .padding(vertical = 32.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌸", fontSize = 52.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    text  = "Delcom Flowers",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color      = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text  = "Bahasa Bunga · Floriography · Hanakotoba",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color      = Color.White.copy(alpha = 0.85f),
                        fontStyle  = FontStyle.Italic,
                        letterSpacing = 0.8.sp,
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }

        // ── Penjelasan Bahasa Bunga ──────────────────────────────────────────
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
                        text  = "Apa itu Bahasa Bunga?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color      = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text  = "Bahasa Bunga (Floriography) adalah seni menyampaikan pesan melalui pilihan bunga. Setiap bunga menyimpan makna simbolis yang unik — dari cinta, kesetiaan, hingga perpisahan — yang telah digunakan lintas budaya selama berabad-abad.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp,
                    ),
                )
            }
        }

        // ── Bunga Populer ────────────────────────────────────────────────────
        Text(
            text  = "🌺 Bunga & Maknanya",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary,
            ),
        )

        val flowers = listOf(
            FlowerCard("🌹", "Mawar Merah",    "Cinta mendalam & gairah",         Color(0xFFFFD6E7)),
            FlowerCard("🌷", "Tulip Pink",     "Kasih sayang & perhatian",        Color(0xFFFFE4EF)),
            FlowerCard("🌸", "Sakura",         "Kecantikan singkat & keanggunan", Color(0xFFFCE4EC)),
            FlowerCard("💜", "Lavender",       "Kekaguman & ketenangan",          Color(0xFFF3E5F5)),
            FlowerCard("🌼", "Chamomile",      "Kesabaran & semangat hidup",      Color(0xFFFFF9C4)),
            FlowerCard("🌻", "Bunga Matahari", "Loyalitas & kebahagiaan",         Color(0xFFFFF3E0)),
        )

        flowers.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                pair.forEach { f ->
                    FlowerMiniCard(f, modifier = Modifier.weight(1f))
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        // ── Budaya Bahasa Bunga ──────────────────────────────────────────────
        Text(
            text  = "🌍 Tradisi di Berbagai Budaya",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary,
            ),
        )

        val cultures = listOf(
            Triple("🇯🇵", "Jepang (Hanakotoba)",   "Sakura = kehidupan singkat; Iris = pesan kesetiaan"),
            Triple("🏰", "Eropa Victoria",          "Mawar merah = cinta rahasia; Pansies = pikiran & memori"),
            Triple("🕌", "Persia & Ottoman",         "Tulip = deklarasi cinta sempurna; Hyacinth = doa"),
        )

        cultures.forEach { (flag, name, desc) ->
            CultureCard(flag = flag, name = name, desc = desc)
        }

        // ── Kutipan ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer,
                        )
                    )
                )
                .padding(20.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text  = "❝",
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text  = "Bunga adalah kata-kata yang bahkan anak-anak pun dapat membacanya.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        color     = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                    ),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = "— Arthur Cleveland Coxe",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color      = MaterialTheme.colorScheme.primary,
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
data class FlowerCard(val emoji: String, val name: String, val meaning: String, val bg: Color)

@Composable
fun FlowerMiniCard(f: FlowerCard, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = f.bg),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier             = Modifier.padding(14.dp),
            horizontalAlignment  = Alignment.CenterHorizontally,
        ) {
            Text(f.emoji, fontSize = 36.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text  = f.name,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = f.meaning,
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
fun CultureCard(flag: String, name: String, desc: String) {
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
            Text(flag, fontSize = 28.sp)
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