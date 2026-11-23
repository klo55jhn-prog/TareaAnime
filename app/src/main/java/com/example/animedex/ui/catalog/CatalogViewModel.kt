package com.example.animedex.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animedex.data.dao.AnimeDao
import com.example.animedex.data.entity.AnimeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CatalogViewModel(private val dao: AnimeDao) : ViewModel() {
    val query = MutableStateFlow("")

    val animes: StateFlow<List<AnimeEntity>> = query
        .debounce(300.milliseconds)
        .flatMapLatest { text -> dao.search(text) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
