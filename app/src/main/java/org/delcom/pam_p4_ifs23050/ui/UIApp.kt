package org.delcom.pam_p4_ifs23051.ui

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
import org.delcom.pam_p4_ifs23051.helper.ConstHelper
import org.delcom.pam_p4_ifs23051.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23051.ui.screens.*
import org.delcom.pam_p4_ifs23051.ui.theme.PetalCream
import org.delcom.pam_p4_ifs23051.ui.viewmodels.FlowerLanguageViewModel
import org.delcom.pam_p4_ifs23051.ui.viewmodels.PlantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController           : NavHostController = rememberNavController(),
    plantViewModel          : PlantViewModel,
    flowerLanguageViewModel : FlowerLanguageViewModel,
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
                .background(PetalCream),
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

            // ── Bahasa Bunga ──────────────────────────────────────────────────
            composable(ConstHelper.RouteNames.FlowerLanguage.path) {
                FlowerLanguageScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    flowerLanguageViewModel = flowerLanguageViewModel,
                )
            }
            composable(ConstHelper.RouteNames.FlowerLanguageAdd.path) {
                FlowerLanguageAddScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    flowerLanguageViewModel = flowerLanguageViewModel,
                )
            }
            composable(
                route     = ConstHelper.RouteNames.FlowerLanguageDetail.path,
                arguments = listOf(navArgument("flowerId") { type = NavType.StringType }),
            ) { back ->
                FlowerLanguageDetailScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    flowerLanguageViewModel = flowerLanguageViewModel,
                    flowerId                = back.arguments?.getString("flowerId") ?: "",
                )
            }
            composable(
                route     = ConstHelper.RouteNames.FlowerLanguageEdit.path,
                arguments = listOf(navArgument("flowerId") { type = NavType.StringType }),
            ) { back ->
                FlowerLanguageEditScreen(
                    navController           = navController,
                    snackbarHost            = snackbarHostState,
                    flowerLanguageViewModel = flowerLanguageViewModel,
                    flowerId                = back.arguments?.getString("flowerId") ?: "",
                )
            }
        }
    }
}