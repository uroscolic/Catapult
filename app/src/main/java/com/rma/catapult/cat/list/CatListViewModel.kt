package com.rma.catapult.cat.list


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.cat.list.api.CatListUiEvent
import com.rma.catapult.cat.list.api.model.CatListUiModel
import com.rma.catapult.cat.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    
    private val _state = MutableStateFlow(CatListState())
    val state = _state.asStateFlow()
    private val events = MutableSharedFlow<CatListUiEvent>()
    private fun setState (reducer : CatListState.() -> CatListState) = _state.getAndUpdate(reducer)
    fun setEvent(event: CatListUiEvent){
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
        loadCats()

        observeCats()
    }


    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{ it ->
                when (it) {
                    CatListUiEvent.ClearSearch -> {
                        setState { copy(searchMode = false, query = "", filteredCats = emptyList()) }
                    }
                    is CatListUiEvent.SearchQueryChanged -> {
                        setState { copy(searchMode = true, query = it.query, filteredCats = cats.filter { cat ->
                            cat.name.contains(it.query, ignoreCase = true) })}

                    }
                }
            }
        }
    }
    private fun observeCats() {
        viewModelScope.launch {
            setState { copy(initialLoading = true) }
            repository.observeAllCats()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(initialLoading = false,
                            cats = it.map { it.asCatListUiModel()}) }
                }

        }
    }
    private fun testRelationship() {
        viewModelScope.launch {
            val catWithImages = repository.getCatWithImages(catId = "abys")
            Log.d("VIDEO", catWithImages.toString())
        }
    }

    private fun loadCats() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchAllCats()
                }
            } catch (e: Exception) {
                setState { copy(error = e)}
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun Cat.asCatListUiModel() = CatListUiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament
    )

}