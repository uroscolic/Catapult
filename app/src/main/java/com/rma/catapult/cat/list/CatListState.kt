package com.rma.catapult.cat.list

import com.rma.catapult.cat.list.api.model.CatListUiModel

data class CatListState(
    val initialLoading : Boolean = true,
    val loading : Boolean = false,
    val searchMode : Boolean = false,
    val query: String = "",
    val cats: List<CatListUiModel> = emptyList(),
    val filteredCats: List<CatListUiModel> = emptyList(),
    val error: Throwable? = null
)
