package com.rma.catapult.catImages.grid

import com.rma.catapult.catImages.model.CatImageUiModel

interface CatImageGridContract {
    data class CatImageGridUiState(
        val loading: Boolean = false,
        val catImages: List<CatImageUiModel> = emptyList()
    )
}