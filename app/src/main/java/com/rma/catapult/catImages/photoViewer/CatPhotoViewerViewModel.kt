package com.rma.catapult.catImages.photoViewer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.catImages.model.CatImageUiModel
import com.rma.catapult.catImages.photoViewer.CatPhotoViewerContract.CatPhotoViewerUiState
import com.rma.catapult.domain.CatImage
import com.rma.catapult.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatPhotoViewerViewModel (
    private val repository: Repository = Repository,
    private val catId: String
) : ViewModel() {

    private val _state = MutableStateFlow(CatPhotoViewerUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatPhotoViewerUiState.() -> CatPhotoViewerUiState) = _state.update(reducer)

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val images = withContext(Dispatchers.IO) {
                    repository.fetchCatImages(id = catId)
                }
                setState { copy(catImages = images.map { it.asCatImageUiModel() }) }
            } catch (error: Exception) {
                // Handle error
            }
            setState { copy(loading = false) }
        }
    }



    private fun CatImage.asCatImageUiModel() = CatImageUiModel(
        id = this.id,
        url = this.url
    )
}