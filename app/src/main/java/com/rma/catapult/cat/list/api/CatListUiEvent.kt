package com.rma.catapult.cat.list.api

sealed class CatListUiEvent {
    data class SearchQueryChanged(val query: String) : CatListUiEvent()
    data object ClearSearch : CatListUiEvent()
}