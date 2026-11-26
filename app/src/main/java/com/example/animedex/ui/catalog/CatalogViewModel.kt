package com.example.animedex.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animedex.data.dao.AnimeDao
import com.example.animedex.data.entity.AnimeEntity
import com.example.animedex.data.repository.SettingsRepository
import com.example.animedex.data.repository.SortOrder
import com.example.animedex.data.repository.UserSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CatalogViewModel(
    private val dao: AnimeDao,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val query = MutableStateFlow("")

    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings(
                sort = SortOrder.RANK,
                minScore = 0f,
                darkTheme = false,
                favorites = emptySet(),
            ),
        )

    val animes: StateFlow<List<AnimeEntity>> = combine(
        query.debounce(300.milliseconds),
        settingsRepository.settings,
    ) { text, userSettings ->
        text to userSettings
    }.flatMapLatest { (text, userSettings) ->
        dao.searchWithFilter(
            text = text,
            minScore = userSettings.minScore.toDouble(),
            sort = userSettings.sort.name,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    val favoriteAnimes: StateFlow<List<AnimeEntity>> = settingsRepository.settings
        .flatMapLatest { userSettings ->
            if (userSettings.favorites.isEmpty()) {
                flowOf(emptyList())
            } else {
                dao.byIds(userSettings.favorites)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun toggleFavorite(animeId: Int) {
        viewModelScope.launch {
            settingsRepository.toggleFavorite(animeId)
        }
    }

    fun setSort(sort: SortOrder) {
        viewModelScope.launch {
            settingsRepository.setSort(sort)
        }
    }

    fun setMinScore(score: Float) {
        viewModelScope.launch {
            settingsRepository.setMinScore(score)
        }
    }

    fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkTheme(darkTheme)
        }
    }
}
