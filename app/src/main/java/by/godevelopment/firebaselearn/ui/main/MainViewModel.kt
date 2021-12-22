package by.godevelopment.firebaselearn.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    /** Convenience method to transform a [Flow] to a [StateFlow]. */
    private fun <T> Flow<T>.toStateFlow(initialValue: T): StateFlow<T> {
        return this.stateIn(viewModelScope, SharingStarted.Lazily, initialValue)
    }
}