package org.delcom.pam_p4_ifs23050.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23050.helper.ConstHelper
import org.delcom.pam_p4_ifs23050.helper.RouteHelper
import org.delcom.pam_p4_ifs23050.ui.theme.DelcomTheme

sealed class MenuBottomNav(
    val route      : String,
    val title      : String,
    val icon       : ImageVector,
    val iconActive : ImageVector,
) {
    object Home : MenuBottomNav(
        ConstHelper.RouteNames.Home.path,
        "Beranda",
        Icons.Outlined.Home,
        Icons.Filled.Home,
    )
    object Plants : MenuBottomNav(
        ConstHelper.RouteNames.Plants.path,
        "Tanaman",
        Icons.Outlined.Nature,
        Icons.Filled.Nature,
    )
    object Zodiac : MenuBottomNav(
        ConstHelper.RouteNames.Zodiac.path,
        "Zodiak",
        Icons.Outlined.AutoAwesome,
        Icons.Filled.AutoAwesome,
    )
    object Profile : MenuBottomNav(
        ConstHelper.RouteNames.Profile.path,
        "Profil",
        Icons.Outlined.Person,
        Icons.Filled.Person,
    )
}

@Composable
fun BottomNavComponent(navController: NavHostController) {
    val items = listOf(
        MenuBottomNav.Home,
        MenuBottomNav.Plants,
        MenuBottomNav.Zodiac,
        MenuBottomNav.Profile,
    )
    val currentRoute = navController.currentDestination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation  = 8.dp,
                shape      = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                spotColor  = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            ),
        shape          = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        tonalElevation = 2.dp,
        color          = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { screen ->
                val selected = currentRoute?.startsWith(screen.route) == true
                val iconSize by animateDpAsState(
                    targetValue = if (selected) 26.dp else 22.dp,
                    label       = "iconSize",
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (selected)
                                Modifier.background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                )
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        onClick = { RouteHelper.to(navController, screen.route, true) },
                        color = androidx.compose.ui.graphics.Color.Transparent,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp),
                        ) {
                            Icon(
                                imageVector        = if (selected) screen.iconActive else screen.icon,
                                contentDescription = screen.title,
                                modifier           = Modifier.size(iconSize),
                                tint               = if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text      = screen.title,
                                style     = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                ),
                                color     = if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                maxLines  = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavComponent() {
    DelcomTheme {
        BottomNavComponent(navController = NavHostController(LocalContext.current))
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBottomNavDark() {
    DelcomTheme(darkTheme = true) {
        BottomNavComponent(navController = NavHostController(LocalContext.current))
    }
}