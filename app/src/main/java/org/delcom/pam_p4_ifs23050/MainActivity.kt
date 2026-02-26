package org.delcom.pam_p4_ifs23050

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.delcom.pam_p4_ifs23050.ui.UIApp
import org.delcom.pam_p4_ifs23050.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23050.ui.viewmodels.ZodiacViewModel
import org.delcom.pam_p4_ifs23050.ui.viewmodels.PlantViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val plantViewModel  : PlantViewModel  by viewModels()
    private val zodiacViewModel : ZodiacViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelcomTheme {
                UIApp(
                    plantViewModel  = plantViewModel,
                    zodiacViewModel = zodiacViewModel,
                )
            }
        }
    }
}