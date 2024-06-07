package com.rma.catapult.catImages.grid

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.catImages.grid.CatImageGridContract.CatImageGridUiState
import com.rma.catapult.catImages.model.CatImageUiModel
import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.cat.repository.Repository
import com.rma.catapult.catImages.db.CatPhoto
import com.rma.catapult.navigation.catId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatImageGridViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: Repository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatImageGridUiState())
    val state = _state.asStateFlow()
    private val catId : String = savedStateHandle.catId
    private fun setState(reducer: CatImageGridUiState.() -> CatImageGridUiState) = _state.update(reducer)

    init {
        fetchImages()
        observeCatImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchCatImages(id = catId)
                }
            } catch (error: Exception) {
                // Handle error
            }
            setState { copy(loading = false) }
        }
    }
    private fun observeCatImages() {
        viewModelScope.launch {
            repository.observeCatImages(catId = catId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(catImages = it.map { it.asCatImageUiModel() }) }
                }
        }
    }



    private fun CatPhoto.asCatImageUiModel() = CatImageUiModel(
        id = this.id,
        url = this.url
    )
}