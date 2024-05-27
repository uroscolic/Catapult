package com.rma.catapult.list

import com.rma.catapult.list.api.model.CatListUiModel

data class CatListState(
    val loading : Boolean = false,
    val searchMode : Boolean = false,
    val query: String = "",
    val cats: List<CatListUiModel> = emptyList(),
    val filteredCats: List<CatListUiModel> = emptyList(),
    val error: Throwable? = null
)
