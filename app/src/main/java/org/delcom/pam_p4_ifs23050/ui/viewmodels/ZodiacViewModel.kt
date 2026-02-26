package org.delcom.pam_p4_ifs23050.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacData
import org.delcom.pam_p4_ifs23050.network.zodiac.service.IZodiacRepository
import javax.inject.Inject

// ── UI States ────────────────────────────────────────────────────────────────

sealed interface ZodiacsUIState {
    data class Success(val data: List<ResponseZodiacData>) : ZodiacsUIState
    data class Error(val message: String) : ZodiacsUIState
    object Loading : ZodiacsUIState
}

sealed interface ZodiacUIState {
    data class Success(val data: ResponseZodiacData) : ZodiacUIState
    data class Error(val message: String) : ZodiacUIState
    object Loading : ZodiacUIState
}

sealed interface ZodiacActionUIState {
    data class Success(val message: String) : ZodiacActionUIState
    data class Error(val message: String) : ZodiacActionUIState
    object Loading : ZodiacActionUIState
}

data class UIStateZodiac(
    val zodiacs      : ZodiacsUIState      = ZodiacsUIState.Loading,
    var zodiac       : ZodiacUIState       = ZodiacUIState.Loading,
    var zodiacAction : ZodiacActionUIState = ZodiacActionUIState.Loading,
)

// ── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
@Keep
class ZodiacViewModel @Inject constructor(
    private val repository: IZodiacRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIStateZodiac())
    val uiState = _uiState.asStateFlow()

    fun getAllZodiacs(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(zodiacs = ZodiacsUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching { repository.getAllZodiacs(search) }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            ZodiacsUIState.Success(resp.data!!.flowers)
                        else
                            ZodiacsUIState.Error(resp.message)
                    },
                    onFailure = { ZodiacsUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(zodiacs = result)
            }
        }
    }

    fun postZodiac(
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(zodiacAction = ZodiacActionUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching {
                    repository.postZodiac(namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
                }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            ZodiacActionUIState.Success(resp.data!!.flowerId)
                        else
                            ZodiacActionUIState.Error(resp.message)
                    },
                    onFailure = { ZodiacActionUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(zodiacAction = result)
            }
        }
    }

    fun getZodiacById(zodiacId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(zodiac = ZodiacUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching { repository.getZodiacById(zodiacId) }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            ZodiacUIState.Success(resp.data!!.flower)
                        else
                            ZodiacUIState.Error(resp.message)
                    },
                    onFailure = { ZodiacUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(zodiac = result)
            }
        }
    }

    fun putZodiac(
        zodiacId   : String,
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(zodiacAction = ZodiacActionUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching {
                    repository.putZodiac(zodiacId, namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
                }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            ZodiacActionUIState.Success(resp.message)
                        else
                            ZodiacActionUIState.Error(resp.message)
                    },
                    onFailure = { ZodiacActionUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(zodiacAction = result)
            }
        }
    }

    fun deleteZodiac(zodiacId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(zodiacAction = ZodiacActionUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching { repository.deleteZodiac(zodiacId) }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            ZodiacActionUIState.Success(resp.message)
                        else
                            ZodiacActionUIState.Error(resp.message)
                    },
                    onFailure = { ZodiacActionUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(zodiacAction = result)
            }
        }
    }
}