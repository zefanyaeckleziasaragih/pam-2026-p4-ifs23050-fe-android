package org.delcom.pam_p4_ifs23050.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23050.R
import org.delcom.pam_p4_ifs23050.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23050.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23050.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23050.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23050.ui.viewmodels.PlantViewModel

// Data profil hardcoded
private val zefanyaProfile = ResponseProfile(
    nama     = "Zefanya Ecklezia Saragih",
    username = "zefanya.ecklesia",
    tentang  = "Saya adalah seorang developer yang memiliki minat besar pada web dan mobile development serta desain UI/UX. Saya antusias mempelajari teknologi baru dan berfokus pada pengembangan aplikasi yang tidak hanya fungsional, tetapi juga memberikan pengalaman pengguna yang baik.",
)

@Composable
fun ProfileScreen(
    navController : NavHostController,
    plantViewModel: PlantViewModel  // tetap diterima agar signature tidak berubah
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Profil", false)
        Box(modifier = Modifier.weight(1f)) {
            ProfileUI(profile = zefanyaProfile)
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun ProfileUI(profile: ResponseProfile) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // Header Profile
        Box(
            modifier         = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Foto Profil — dari app/src/main/res/drawable/profile.png
                Image(
                    painter            = painterResource(R.drawable.profile),
                    contentDescription = "Foto Profil",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text       = profile.nama,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text     = profile.username,
                    fontSize = 16.sp,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bio Section
        Card(
            modifier  = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape     = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Tentang Saya",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.fillMaxWidth(),
                    color      = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    profile.tentang,
                    fontSize = 15.sp,
                    color    = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileUI() {
    DelcomTheme {
        ProfileUI(profile = zefanyaProfile)
    }
}