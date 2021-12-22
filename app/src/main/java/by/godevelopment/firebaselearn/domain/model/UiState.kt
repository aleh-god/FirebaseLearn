package by.godevelopment.firebaselearn.domain.model

data class UiState<T>(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: T
)