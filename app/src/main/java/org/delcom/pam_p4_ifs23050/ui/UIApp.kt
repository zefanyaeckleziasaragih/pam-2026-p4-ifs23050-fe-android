package org.delcom.pam_p4_ifs23050.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23050.helper.ConstHelper
import org.delcom.pam_p4_ifs23050.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23050.ui.screens.*
import org.delcom.pam_p4_ifs23050.ui.theme.StarlightWhite
import org.delcom.pam_p4_ifs23050.ui.viewmodels.ZodiacViewModel
import org.delcom.pam_p4_ifs23050.ui.viewmodels.PlantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController   : NavHostController = rememberNavController(),
    plantViewModel  : PlantViewModel,
    zodiacViewModel : ZodiacViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                CustomSnackbar(
                    snackbarData = snackbarData,
                    onDismiss    = { snackbarHostState.currentSnackbarData?.dismiss() },
                )
            }
        },
    ) { _ ->
        NavHost(
            navController    = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier         = Modifier
                .fillMaxSize()
                .background(StarlightWhite),
        ) {

            // ── Home ──────────────────────────────────────────────────────────
            composable(ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }

            // ── Profile ───────────────────────────────────────────────────────
            composable(ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(navController = navController, plantViewModel = plantViewModel)
            }

            // ── Plants ────────────────────────────────────────────────────────
            composable(ConstHelper.RouteNames.Plants.path) {
                PlantsScreen(navController = navController, plantViewModel = plantViewModel)
            }
            composable(ConstHelper.RouteNames.PlantsAdd.path) {
                PlantsAddScreen(
                    navController  = navController,
                    snackbarHost   = snackbarHostState,
                    plantViewModel = plantViewModel,
                )
            }
            composable(
                route     = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType }),
            ) { back ->
                PlantsDetailScreen(
                    navController  = navController,
                    snackbarHost   = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId        = back.arguments?.getString("plantId") ?: "",
                )
            }
            composable(
                route     = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType }),
            ) { back ->
                PlantsEditScreen(
                    navController  = navController,
                    snackbarHost   = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId        = back.arguments?.getString("plantId") ?: "",
                )
            }

            // ── Zodiac (Rasi Bintang) ─────────────────────────────────────────
            composable(ConstHelper.RouteNames.Zodiac.path) {
                ZodiacScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    zodiacViewModel = zodiacViewModel,
                )
            }
            composable(ConstHelper.RouteNames.ZodiacAdd.path) {
                ZodiacAddScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    zodiacViewModel = zodiacViewModel,
                )
            }
            composable(
                route     = ConstHelper.RouteNames.ZodiacDetail.path,
                arguments = listOf(navArgument("zodiacId") { type = NavType.StringType }),
            ) { back ->
                ZodiacDetailScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    zodiacViewModel = zodiacViewModel,
                    zodiacId                = back.arguments?.getString("zodiacId") ?: "",
                )
            }
            composable(
                route     = ConstHelper.RouteNames.ZodiacEdit.path,
                arguments = listOf(navArgument("zodiacId") { type = NavType.StringType }),
            ) { back ->
                ZodiacEditScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    zodiacViewModel = zodiacViewModel,
                    zodiacId                = back.arguments?.getString("zodiacId") ?: "",
                )
            }
        }
    }
}