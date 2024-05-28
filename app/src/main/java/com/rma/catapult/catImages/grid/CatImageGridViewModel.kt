package com.rma.catapult.catImages.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.catImages.grid.CatImageGridContract.CatImageGridUiState
import com.rma.catapult.catImages.model.CatImageUiModel
import com.rma.catapult.domain.CatImage
import com.rma.catapult.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatImageGridViewModel (
    private val repository: Repository = Repository,
    private val catId: String
) : ViewModel() {

    private val _state = MutableStateFlow(CatImageGridUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatImageGridUiState.() -> CatImageGridUiState) = _state.update(reducer)

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
                Log.d("aaaaaa", images.size.toString())
                setState { copy(catImages = images.map { it.asCatImageUiModel() }) }
            } catch (error: Exception) {
                // TODO Handle error
            }
            setState { copy(loading = false) }
        }
    }



    private fun CatImage.asCatImageUiModel() = CatImageUiModel(
        id = this.id,
        url = this.url
    )
}