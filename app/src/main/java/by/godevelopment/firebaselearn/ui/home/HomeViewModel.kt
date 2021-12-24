package by.godevelopment.firebaselearn.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val clickEvent = MutableStateFlow<Boolean>(false)

    fun onClick() {
        clickEvent.value = true
    }
}