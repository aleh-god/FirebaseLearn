package by.godevelopment.firebaselearn.ui.home

import androidx.lifecycle.ViewModel
import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) : ViewModel() {

    val clickEvent = MutableStateFlow<Boolean>(false)

    fun onClick() {
        firebaseHandler.logOut()
        clickEvent.value = true
    }
}