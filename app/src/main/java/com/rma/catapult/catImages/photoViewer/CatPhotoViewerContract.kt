package com.rma.catapult.catImages.photoViewer


import com.rma.catapult.catImages.model.CatImageUiModel

interface CatPhotoViewerContract {
    data class CatPhotoViewerUiState(
        val loading: Boolean = false,
        val catImages: List<CatImageUiModel> = emptyList()
    )
}