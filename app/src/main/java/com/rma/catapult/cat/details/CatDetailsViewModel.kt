package com.rma.catapult.cat.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.cat.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CatDetailsViewModel constructor(
    private val catId: String,
    private val repository: Repository = Repository
) : ViewModel() {

    private val _state = MutableStateFlow(CatDetailsState(catId = catId))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsState.() -> CatDetailsState) {
        _state.getAndUpdate(reducer)
    }
    init {
        fetchCatDetails()
    }

    private fun fetchCatDetails() {
        viewModelScope.launch {
            try {
                setState { copy(loading = true) }
                withContext(Dispatchers.IO){
                    val cat = repository.fetchCatById(catId)
                    setState { copy(cat = cat, loading = false) }
                }
            }
            catch (e: IOException) {
                setState { copy(error = e)}
            }
            finally {
                setState { copy(loading = false) }
            }
        }
    }
}