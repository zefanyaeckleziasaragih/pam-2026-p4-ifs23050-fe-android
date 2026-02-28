package org.delcom.pam_p4_ifs23050.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// ── Warna zodiak (mirror dari Color.kt agar tidak import circular) ──────────
private val StarGold      = Color(0xFFFFD600)
private val CosmicPurple  = Color(0xFF7B1FA2)
private val PurpleLight   = Color(0xFFCE93D8)
private val DeepSpace     = Color(0xFF0D0820)

/**
 * Animasi loading zodiak:
 * - Lingkaran orbit berputar searah jarum jam
 * - 12 titik bintang (representasi 12 zodiak) pada orbit
 * - 2 lapisan ripple yang mengembang & memudar
 * - Bintang 6 sudut di tengah yang berdenyut
 */
@Composable
fun ZodiacLoading(
    modifier        : Modifier = Modifier,
    orbitRadius     : Dp       = 60.dp,
    containerSize   : Dp       = 180.dp,
    animDuration    : Int      = 4000,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "zodiac_loading")

    // Rotasi orbit
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue   = 0f,
        targetValue    = 360f,
        animationSpec  = infiniteRepeatable(
            animation = tween(animDuration, easing = LinearEasing),
        ),
        label = "orbit_rotation",
    )

    // Pulsasi bintang tengah
    val pulse by infiniteTransition.animateFloat(
        initialValue  = 0.7f,
        targetValue   = 1.0f,
        animationSpec = infiniteRepeatable(
            animation  = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "center_pulse",
    )

    // Ripple 1
    val ripple1 by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animDuration, easing = LinearEasing),
        ),
        label = "ripple1",
    )

    // Ripple 2 (offset setengah siklus)
    val ripple2 by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation      = tween(animDuration, easing = LinearEasing),
            initialStartOffset = StartOffset(animDuration / 2),
        ),
        label = "ripple2",
    )

    Box(
        modifier         = modifier.size(containerSize),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val orbitPx = orbitRadius.toPx()
            val maxRipple = orbitPx * 1.45f

            // ── Ripple 1 ──────────────────────────────────────────────────
            drawCircle(
                color  = StarGold.copy(alpha = (1f - ripple1) * 0.35f),
                radius = orbitPx * 0.6f + ripple1 * (maxRipple - orbitPx * 0.6f),
                center = Offset(cx, cy),
                style  = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
            )

            // ── Ripple 2 ──────────────────────────────────────────────────
            drawCircle(
                color  = CosmicPurple.copy(alpha = (1f - ripple2) * 0.4f),
                radius = orbitPx * 0.6f + ripple2 * (maxRipple - orbitPx * 0.6f),
                center = Offset(cx, cy),
                style  = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx()),
            )

            // ── Orbit ring (berputar) ─────────────────────────────────────
            rotate(rotationAngle, pivot = Offset(cx, cy)) {
                // Ring tipis
                drawCircle(
                    color  = PurpleLight.copy(alpha = 0.3f),
                    radius = orbitPx,
                    center = Offset(cx, cy),
                    style  = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()),
                )

                // 12 titik zodiak
                repeat(12) { i ->
                    val angleRad = Math.toRadians((i * 30).toDouble())
                    val dotX = cx + orbitPx * cos(angleRad).toFloat()
                    val dotY = cy + orbitPx * sin(angleRad).toFloat()
                    val isGold = i % 2 == 0

                    drawCircle(
                        color  = if (isGold) StarGold else PurpleLight,
                        radius = if (isGold) 4.dp.toPx() else 2.5.dp.toPx(),
                        center = Offset(dotX, dotY),
                    )
                    // Glow di dot emas
                    if (isGold) {
                        drawCircle(
                            color  = StarGold.copy(alpha = 0.25f),
                            radius = 7.dp.toPx(),
                            center = Offset(dotX, dotY),
                        )
                    }
                }
            }

            // ── Bintang 6 sudut di tengah (static, pulsating) ────────────
            drawSixPointStar(
                center  = Offset(cx, cy),
                radius  = 18.dp.toPx() * pulse,
                color1  = StarGold,
                color2  = PurpleLight,
            )

            // Titik tengah putih
            drawCircle(
                color  = Color.White,
                radius = 4.dp.toPx() * pulse,
                center = Offset(cx, cy),
            )
            drawCircle(
                color  = Color.White.copy(alpha = 0.3f),
                radius = 8.dp.toPx() * pulse,
                center = Offset(cx, cy),
            )
        }
    }
}

/** Gambar bintang 6 sudut dengan 2 segitiga berlawanan. */
private fun DrawScope.drawSixPointStar(
    center  : Offset,
    radius  : Float,
    color1  : Color,
    color2  : Color,
) {
    val innerR = radius * 0.5f

    // Segitiga atas (kuning/emas)
    val triUp = buildTrianglePath(center, radius, 270f)
    drawPath(path = triUp, color = color1.copy(alpha = 0.9f))

    // Segitiga bawah (ungu)
    val triDown = buildTrianglePath(center, radius, 90f)
    drawPath(path = triDown, color = color2.copy(alpha = 0.85f))

    // Center hexagon cover agar terlihat bintang 6
    drawCircle(
        color  = Color(0xFF3A0070).copy(alpha = 0.6f),
        radius = innerR,
        center = center,
    )
}

private fun buildTrianglePath(
    center     : Offset,
    radius     : Float,
    startAngle : Float,
): androidx.compose.ui.graphics.Path {
    val path = androidx.compose.ui.graphics.Path()
    val angles = listOf(startAngle, startAngle + 120f, startAngle + 240f)
    angles.forEachIndexed { i, angleDeg ->
        val rad = Math.toRadians(angleDeg.toDouble())
        val x   = center.x + radius * cos(rad).toFloat()
        val y   = center.y + radius * sin(rad).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

// ── Public composable ────────────────────────────────────────────────────────

@Composable
fun LoadingUI() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ZodiacLoading()

            Spacer(Modifier.height(24.dp))

            Text(
                text  = "Delcom Zodiac",
                style = MaterialTheme.typography.titleMedium.copy(
                    color     = Color(0xFFFFD600),
                    fontSize  = 18.sp,
                    fontStyle = FontStyle.Italic,
                ),
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text  = "Memuat data rasi bintang...",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFCE93D8),
                ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0820)
@Composable
fun PreviewLoadingUI() {
    LoadingUI()
}