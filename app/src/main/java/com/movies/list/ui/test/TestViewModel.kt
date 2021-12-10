package com.movies.list.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {

    private val sortOrder = MutableStateFlow("year")

    val myData = sortOrder.flatMapLatest {
        Timber.d("Sort order changed to $it")
        flowOf("Sort order is $it")
    }.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        1
    )

    init {
        viewModelScope.launch {
            delay(4000)
            sortOrder.value = "rating"
        }
    }

}

