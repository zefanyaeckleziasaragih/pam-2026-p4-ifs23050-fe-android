package org.delcom.pam_p4_ifs23051.ui.viewmodels

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
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguageData
import org.delcom.pam_p4_ifs23051.network.flower.service.IFlowerLanguageRepository
import javax.inject.Inject

// ── UI States ────────────────────────────────────────────────────────────────

sealed interface FlowersUIState {
    data class Success(val data: List<ResponseFlowerLanguageData>) : FlowersUIState
    data class Error(val message: String) : FlowersUIState
    object Loading : FlowersUIState
}

sealed interface FlowerUIState {
    data class Success(val data: ResponseFlowerLanguageData) : FlowerUIState
    data class Error(val message: String) : FlowerUIState
    object Loading : FlowerUIState
}

sealed interface FlowerActionUIState {
    data class Success(val message: String) : FlowerActionUIState
    data class Error(val message: String) : FlowerActionUIState
    object Loading : FlowerActionUIState
}

data class UIStateFlowerLanguage(
    val flowers      : FlowersUIState      = FlowersUIState.Loading,
    var flower       : FlowerUIState       = FlowerUIState.Loading,
    var flowerAction : FlowerActionUIState = FlowerActionUIState.Loading,
)

// ── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
@Keep
class FlowerLanguageViewModel @Inject constructor(
    private val repository: IFlowerLanguageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIStateFlowerLanguage())
    val uiState = _uiState.asStateFlow()

    /** Ambil semua bunga (opsional: dengan kata kunci pencarian) */
    fun getAllFlowers(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(flowers = FlowersUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching { repository.getAllFlowers(search) }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            FlowersUIState.Success(resp.data!!.flowers)
                        else
                            FlowersUIState.Error(resp.message)
                    },
                    onFailure = { FlowersUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(flowers = result)
            }
        }
    }

    /** Tambah data bunga baru */
    fun postFlower(
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(flowerAction = FlowerActionUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching {
                    repository.postFlower(namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
                }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            FlowerActionUIState.Success(resp.data!!.flowerId)
                        else
                            FlowerActionUIState.Error(resp.message)
                    },
                    onFailure = { FlowerActionUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(flowerAction = result)
            }
        }
    }

    /** Ambil satu data bunga berdasarkan ID */
    fun getFlowerById(flowerId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(flower = FlowerUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching { repository.getFlowerById(flowerId) }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            FlowerUIState.Success(resp.data!!.flower)
                        else
                            FlowerUIState.Error(resp.message)
                    },
                    onFailure = { FlowerUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(flower = result)
            }
        }
    }

    /** Ubah data bunga */
    fun putFlower(
        flowerId   : String,
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part? = null,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(flowerAction = FlowerActionUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching {
                    repository.putFlower(flowerId, namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
                }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            FlowerActionUIState.Success(resp.message)
                        else
                            FlowerActionUIState.Error(resp.message)
                    },
                    onFailure = { FlowerActionUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(flowerAction = result)
            }
        }
    }

    /** Hapus data bunga */
    fun deleteFlower(flowerId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(flowerAction = FlowerActionUIState.Loading) }
            _uiState.update { state ->
                val result = runCatching { repository.deleteFlower(flowerId) }.fold(
                    onSuccess = { resp ->
                        if (resp.status == "success")
                            FlowerActionUIState.Success(resp.message)
                        else
                            FlowerActionUIState.Error(resp.message)
                    },
                    onFailure = { FlowerActionUIState.Error(it.message ?: "Unknown error") },
                )
                state.copy(flowerAction = result)
            }
        }
    }
}