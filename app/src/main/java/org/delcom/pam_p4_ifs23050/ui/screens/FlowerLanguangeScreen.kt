package org.delcom.pam_p4_ifs23051.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23051.R
import org.delcom.pam_p4_ifs23051.helper.ConstHelper
import org.delcom.pam_p4_ifs23051.helper.RouteHelper
import org.delcom.pam_p4_ifs23051.helper.SuspendHelper
import org.delcom.pam_p4_ifs23051.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23051.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23051.helper.ToolsHelper.getFlowerImageUrl
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguageData
import org.delcom.pam_p4_ifs23051.ui.components.*
import org.delcom.pam_p4_ifs23051.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23051.ui.viewmodels.*

// ═════════════════════════════════════════════════════════════════════════════
// LIST SCREEN
// ═════════════════════════════════════════════════════════════════════════════
@Composable
fun FlowerLanguageScreen(
    navController        : NavHostController,
    snackbarHost         : SnackbarHostState,
    flowerLanguageViewModel : FlowerLanguageViewModel,
) {
    val uiState by flowerLanguageViewModel.uiState.collectAsState()
    var isLoading  by remember { mutableStateOf(false) }
    var flowers    by remember { mutableStateOf<List<ResponseFlowerLanguageData>>(emptyList()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    fun fetchFlowers() {
        isLoading = true
        flowerLanguageViewModel.getAllFlowers(searchQuery.text.ifBlank { null })
    }

    LaunchedEffect(Unit) { fetchFlowers() }

    LaunchedEffect(uiState.flowers) {
        if (uiState.flowers !is FlowersUIState.Loading) {
            isLoading = false
            flowers = if (uiState.flowers is FlowersUIState.Success)
                (uiState.flowers as FlowersUIState.Success).data
            else emptyList()
        }
    }

    if (isLoading) { LoadingUI(); return }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBarComponent(
            navController       = navController,
            title               = "Bahasa Bunga",
            showBackButton      = false,
            withSearch          = true,
            searchQuery         = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchAction      = { fetchFlowers() },
        )

        Box(modifier = Modifier.weight(1f)) {
            FlowerLanguageListUI(flowers = flowers) { id ->
                RouteHelper.to(navController, "flower-language/$id")
            }
            FloatingActionButton(
                onClick = { RouteHelper.to(navController, ConstHelper.RouteNames.FlowerLanguageAdd.path) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Bahasa Bunga")
            }
        }

        BottomNavComponent(navController = navController)
    }
}

@Composable
fun FlowerLanguageListUI(
    flowers : List<ResponseFlowerLanguageData>,
    onOpen  : (String) -> Unit,
) {
    if (flowers.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌸", fontSize = 72.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Belum ada data bahasa bunga",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tekan tombol + untuk menambahkan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
        return
    }

    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(flowers) { flower ->
            FlowerLanguageItemCard(flower = flower, onOpen = onOpen)
        }
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
fun FlowerLanguageItemCard(
    flower : ResponseFlowerLanguageData,
    onOpen : (String) -> Unit,
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onOpen(flower.id) },
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            // Gambar bunga
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model              = getFlowerImageUrl(flower.id),
                    contentDescription = flower.namaUmum,
                    placeholder        = painterResource(R.drawable.img_placeholder),
                    error              = painterResource(R.drawable.img_placeholder),
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize(),
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = flower.namaUmum,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text  = flower.namaLatin,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
                Spacer(Modifier.height(6.dp))
                // Makna chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector    = Icons.Default.LocalFlorist,
                            contentDescription = null,
                            modifier       = Modifier.size(12.dp),
                            tint           = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text  = flower.makna,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color      = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Icon(
                imageVector    = Icons.Default.ChevronRight,
                contentDescription = "Buka",
                tint           = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// DETAIL SCREEN
// ═════════════════════════════════════════════════════════════════════════════
@Composable
fun FlowerLanguageDetailScreen(
    navController           : NavHostController,
    snackbarHost            : SnackbarHostState,
    flowerLanguageViewModel : FlowerLanguageViewModel,
    flowerId                : String,
) {
    val uiState       by flowerLanguageViewModel.uiState.collectAsState()
    var isLoading     by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
    var flower        by remember { mutableStateOf<ResponseFlowerLanguageData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiState.flower       = FlowerUIState.Loading
        uiState.flowerAction = FlowerActionUIState.Loading
        flowerLanguageViewModel.getFlowerById(flowerId)
    }

    LaunchedEffect(uiState.flower) {
        if (uiState.flower !is FlowerUIState.Loading) {
            if (uiState.flower is FlowerUIState.Success)
                flower = (uiState.flower as FlowerUIState.Success).data
            else RouteHelper.back(navController)
            isLoading = false
        }
    }

    LaunchedEffect(uiState.flowerAction) {
        when (val s = uiState.flowerAction) {
            is FlowerActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.SUCCESS, s.message)
                RouteHelper.to(navController, ConstHelper.RouteNames.FlowerLanguage.path, true)
                isLoading = false
            }
            is FlowerActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.ERROR, s.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading || flower == null) { LoadingUI(); return }

    val menuItems = listOf(
        TopAppBarMenuItem(
            text    = "Ubah Data",
            icon    = Icons.Filled.Edit,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.FlowerLanguageEdit.path.replace("{flowerId}", flower!!.id),
                )
            },
        ),
        TopAppBarMenuItem(
            text            = "Hapus Data",
            icon            = Icons.Filled.Delete,
            isDestructive   = true,
            onClick         = { isConfirmDelete = true },
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBarComponent(
            navController   = navController,
            title           = flower!!.namaUmum,
            showBackButton  = true,
            customMenuItems = menuItems,
        )
        Box(modifier = Modifier.weight(1f)) {
            FlowerLanguageDetailUI(flower = flower!!)
            BottomDialog(
                type            = BottomDialogType.ERROR,
                show            = isConfirmDelete,
                onDismiss       = { isConfirmDelete = false },
                title           = "Hapus Data",
                message         = "Yakin ingin menghapus \"${flower!!.namaUmum}\"?",
                confirmText     = "Ya, Hapus",
                onConfirm       = { isLoading = true; flowerLanguageViewModel.deleteFlower(flowerId) },
                cancelText      = "Batal",
                destructiveAction = true,
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun FlowerLanguageDetailUI(flower: ResponseFlowerLanguageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Header gambar + gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
        ) {
            AsyncImage(
                model              = getFlowerImageUrl(flower.id),
                contentDescription = flower.namaUmum,
                placeholder        = painterResource(R.drawable.img_placeholder),
                error              = painterResource(R.drawable.img_placeholder),
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize(),
            )
            // gradient overlay bawah
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.55f to Color.Transparent,
                            1f to Color(0xBB1C0011),
                        )
                    ),
            )
            // Nama di atas gambar
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
            ) {
                Text(
                    text  = flower.namaUmum,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color      = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )
                Text(
                    text  = flower.namaLatin,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color     = Color.White.copy(alpha = 0.8f),
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {

            // Makna — highlight card
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                elevation = CardDefaults.cardElevation(3.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("✨", fontSize = 26.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Makna",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                            ),
                        )
                        Text(
                            flower.makna,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color      = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                }
            }

            // Asal Budaya
            DetailSectionCard(icon = "🌍", title = "Asal Budaya", content = flower.asalBudaya)

            // Deskripsi
            DetailSectionCard(icon = "📖", title = "Deskripsi", content = flower.deskripsi)

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DetailSectionCard(icon: String, title: String, content: String) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                ),
            )
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// ADD SCREEN
// ═════════════════════════════════════════════════════════════════════════════
@Composable
fun FlowerLanguageAddScreen(
    navController           : NavHostController,
    snackbarHost            : SnackbarHostState,
    flowerLanguageViewModel : FlowerLanguageViewModel,
) {
    val uiState by flowerLanguageViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        uiState.flowerAction = FlowerActionUIState.Loading
    }

    fun onSave(
        context    : Context,
        namaUmum   : String,
        namaLatin  : String,
        makna      : String,
        asalBudaya : String,
        deskripsi  : String,
        file       : Uri?,
    ) {
        isLoading = true
        flowerLanguageViewModel.postFlower(
            namaUmum   = namaUmum.toRequestBodyText(),
            namaLatin  = namaLatin.toRequestBodyText(),
            makna      = makna.toRequestBodyText(),
            asalBudaya = asalBudaya.toRequestBodyText(),
            deskripsi  = deskripsi.toRequestBodyText(),
            file       = uriToMultipart(context, file!!, "file"),
        )
    }

    LaunchedEffect(uiState.flowerAction) {
        when (val s = uiState.flowerAction) {
            is FlowerActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.SUCCESS, "Berhasil ditambahkan!")
                RouteHelper.to(navController, ConstHelper.RouteNames.FlowerLanguage.path, true)
                isLoading = false
            }
            is FlowerActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.ERROR, s.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading) { LoadingUI(); return }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBarComponent(navController = navController, title = "Tambah Bahasa Bunga", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            FlowerLanguageFormUI(initialData = null, requireImage = true, onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// EDIT SCREEN
// ═════════════════════════════════════════════════════════════════════════════
@Composable
fun FlowerLanguageEditScreen(
    navController           : NavHostController,
    snackbarHost            : SnackbarHostState,
    flowerLanguageViewModel : FlowerLanguageViewModel,
    flowerId                : String,
) {
    val uiState   by flowerLanguageViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var flower    by remember { mutableStateOf<ResponseFlowerLanguageData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiState.flower       = FlowerUIState.Loading
        uiState.flowerAction = FlowerActionUIState.Loading
        flowerLanguageViewModel.getFlowerById(flowerId)
    }

    LaunchedEffect(uiState.flower) {
        if (uiState.flower !is FlowerUIState.Loading) {
            if (uiState.flower is FlowerUIState.Success)
                flower = (uiState.flower as FlowerUIState.Success).data
            else RouteHelper.back(navController)
            isLoading = false
        }
    }

    fun onSave(
        context    : Context,
        namaUmum   : String,
        namaLatin  : String,
        makna      : String,
        asalBudaya : String,
        deskripsi  : String,
        file       : Uri?,
    ) {
        isLoading = true
        val filePart: MultipartBody.Part? = file?.let { uriToMultipart(context, it, "file") }
        flowerLanguageViewModel.putFlower(
            flowerId   = flowerId,
            namaUmum   = namaUmum.toRequestBodyText(),
            namaLatin  = namaLatin.toRequestBodyText(),
            makna      = makna.toRequestBodyText(),
            asalBudaya = asalBudaya.toRequestBodyText(),
            deskripsi  = deskripsi.toRequestBodyText(),
            file       = filePart,
        )
    }

    LaunchedEffect(uiState.flowerAction) {
        when (val s = uiState.flowerAction) {
            is FlowerActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.SUCCESS, s.message)
                RouteHelper.to(
                    navController = navController,
                    destination   = ConstHelper.RouteNames.FlowerLanguageDetail.path.replace("{flowerId}", flowerId),
                    popUpTo       = ConstHelper.RouteNames.FlowerLanguageDetail.path.replace("{flowerId}", flowerId),
                    removeBackStack = true,
                )
                isLoading = false
            }
            is FlowerActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.ERROR, s.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading || flower == null) { LoadingUI(); return }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBarComponent(navController = navController, title = "Ubah Bahasa Bunga", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            FlowerLanguageFormUI(initialData = flower, requireImage = false, onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// SHARED FORM UI
// ═════════════════════════════════════════════════════════════════════════════
@Composable
fun FlowerLanguageFormUI(
    initialData  : ResponseFlowerLanguageData?,
    requireImage : Boolean,
    onSave       : (Context, String, String, String, String, String, Uri?) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val context      = LocalContext.current

    var dataNamaUmum   by remember { mutableStateOf(initialData?.namaUmum ?: "") }
    var dataNamaLatin  by remember { mutableStateOf(initialData?.namaLatin ?: "") }
    var dataMakna      by remember { mutableStateOf(initialData?.makna ?: "") }
    var dataAsalBudaya by remember { mutableStateOf(initialData?.asalBudaya ?: "") }
    var dataDeskripsi  by remember { mutableStateOf(initialData?.deskripsi ?: "") }
    var dataFile       by remember { mutableStateOf<Uri?>(null) }
    var errorMsg       by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> dataFile = uri }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor       = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor     = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor     = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor   = MaterialTheme.colorScheme.outline,
        cursorColor            = MaterialTheme.colorScheme.primary,
        focusedLabelColor      = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor    = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {

        // ── Image picker ────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                when {
                    dataFile != null -> AsyncImage(
                        model              = dataFile,
                        contentDescription = "Pratinjau",
                        placeholder        = painterResource(R.drawable.img_placeholder),
                        error              = painterResource(R.drawable.img_placeholder),
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize(),
                    )
                    initialData != null -> AsyncImage(
                        model              = getFlowerImageUrl(initialData.id),
                        contentDescription = "Gambar saat ini",
                        placeholder        = painterResource(R.drawable.img_placeholder),
                        error              = painterResource(R.drawable.img_placeholder),
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize(),
                    )
                    else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier           = Modifier.size(36.dp),
                            tint               = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Pilih Gambar",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                if (initialData != null) "Tap untuk mengganti gambar (opsional)"
                else "Tap untuk memilih gambar *",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // ── Form fields ─────────────────────────────────────────────────────
        OutlinedTextField(
            value         = dataNamaUmum,
            onValueChange = { dataNamaUmum = it },
            label         = { Text("Nama Umum *") },
            colors        = fieldColors,
            modifier      = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine    = true,
        )

        OutlinedTextField(
            value         = dataNamaLatin,
            onValueChange = { dataNamaLatin = it },
            label         = { Text("Nama Latin *") },
            placeholder   = { Text("Contoh: Rosa damascena", fontStyle = FontStyle.Italic) },
            colors        = fieldColors,
            modifier      = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine    = true,
        )

        OutlinedTextField(
            value         = dataMakna,
            onValueChange = { dataMakna = it },
            label         = { Text("Makna Bunga *") },
            placeholder   = { Text("Contoh: Cinta & Gairah") },
            colors        = fieldColors,
            modifier      = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine    = true,
        )

        OutlinedTextField(
            value         = dataAsalBudaya,
            onValueChange = { dataAsalBudaya = it },
            label         = { Text("Asal Budaya *") },
            placeholder   = { Text("Contoh: Jepang, Eropa Victoria") },
            colors        = fieldColors,
            modifier      = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine    = true,
        )

        OutlinedTextField(
            value         = dataDeskripsi,
            onValueChange = { dataDeskripsi = it },
            label         = { Text("Deskripsi *") },
            colors        = fieldColors,
            modifier      = Modifier
                .fillMaxWidth()
                .height(130.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction    = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 6,
            minLines = 3,
        )

        Spacer(Modifier.height(72.dp))
    }

    // ── FAB Simpan ──────────────────────────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                when {
                    dataNamaUmum.isBlank()   -> errorMsg = "Nama umum tidak boleh kosong!"
                    dataNamaLatin.isBlank()  -> errorMsg = "Nama latin tidak boleh kosong!"
                    dataMakna.isBlank()      -> errorMsg = "Makna tidak boleh kosong!"
                    dataAsalBudaya.isBlank() -> errorMsg = "Asal budaya tidak boleh kosong!"
                    dataDeskripsi.isBlank()  -> errorMsg = "Deskripsi tidak boleh kosong!"
                    requireImage && dataFile == null -> errorMsg = "Gambar tidak boleh kosong!"
                    else -> onSave(
                        context, dataNamaUmum, dataNamaLatin,
                        dataMakna, dataAsalBudaya, dataDeskripsi, dataFile,
                    )
                }
            },
            modifier       = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor   = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }

    if (errorMsg.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { errorMsg = "" },
            title            = { Text("Perhatian") },
            text             = { Text(errorMsg) },
            confirmButton    = {
                TextButton(onClick = { errorMsg = "" }) { Text("OK") }
            },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun PreviewFlowerLanguageListUI() {
    DelcomTheme {
        FlowerLanguageListUI(flowers = emptyList(), onOpen = {})
    }
}