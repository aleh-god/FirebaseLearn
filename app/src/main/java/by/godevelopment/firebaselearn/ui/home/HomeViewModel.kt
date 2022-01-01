package by.godevelopment.firebaselearn.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.firebaselearn.common.EMPTY_STRING_VALUE
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.domain.usecase.GetNameCurrentUserUseCase
import by.godevelopment.firebaselearn.domain.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getNameCurrentUserUseCase: GetNameCurrentUserUseCase
) : ViewModel() {
    // input flow
    val welcomeMessage = MutableStateFlow(EMPTY_STRING_VALUE)

    // output flow
    val clickEvent = MutableStateFlow<Boolean>(false)

    init {
        viewModelScope.launch {
            welcomeMessage.value = "Welcome, ${getNameCurrentUserUseCase()}"
        }
    }

    fun onClick() {
        Log.i(LOG_KEY, "HomeViewModel onClick")
        logOutUseCase()
        clickEvent.value = true
    }
}
