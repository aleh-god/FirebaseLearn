package by.godevelopment.firebaselearn.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import by.godevelopment.firebaselearn.domain.model.DataState
import by.godevelopment.firebaselearn.domain.model.EventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseHandler: FirebaseHandler
): ViewModel() {
    // input Flow
    val email =  MutableStateFlow("")
    val password =  MutableStateFlow("")
    private val onClickRegEvent =  MutableStateFlow(false)

    // output Flow
    private val _eventState: MutableStateFlow<EventState> =  MutableStateFlow(EventState.Hold)
    val eventState: StateFlow<EventState> = _eventState

    // для прогресс бара
    val isLoading =  MutableStateFlow(false)

    val isEnableBtnReg: StateFlow<Boolean> = combine(email, password) {
            email, pass ->
        Log.i(LOG_KEY, "RegisterViewModel userState.map $email + $pass")
        if (email.isNullOrEmpty() && pass.isNullOrEmpty()) {
            Log.i(LOG_KEY, "RegisterViewModel EventState.Alert create")
            _eventState.value = EventState.Alert(
                alertMessage = "Поля пусты!"
            )
            delay(100)
            _eventState.value = EventState.Hold
            false
        } else {
            true
        }
    }.toStateFlow(false)


    init {
        viewModelScope.launch {
            onClickRegEvent.collect {
                Log.i(LOG_KEY, "RegisterViewModel onClickLoginEvent.collect $it")
                if (it) {
                    firebaseHandler.createUserWithEmailAndPassword(
                        email.value,
                        password.value
                    ).collect { state ->
                        Log.i(LOG_KEY, "RegisterViewModel signInWithEmailAndPassword.collect $state")
                        when (state) {
                            is DataState.Loading -> {
                                isLoading.value = true
                            }
                            is DataState.Success -> {
                                _eventState.value = EventState.RunNav(R.id.action_registerFragment_to_main_fragment)
                                isLoading.value = false
                            }
                            is DataState.Error -> {
                                _eventState.value = EventState.Alert(
                                    alertMessage = "Проблема с сервером авторизации!"
                                )
                                isLoading.value = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun onClickReg() {
        Log.i(LOG_KEY, "RegisterViewModel onClickReg()")
        onClickRegEvent.value = true
    }

    fun onClickBack() {
        Log.i(LOG_KEY, "RegisterViewModel onClickBack()")
        _eventState.value = EventState.RunNav(R.id.action_registerFragment_to_main_fragment)
    }

    /** Convenience method to transform a [Flow] to a [StateFlow]. */
    private fun <T> Flow<T>.toStateFlow(initialValue: T): StateFlow<T> {
        return this.stateIn(viewModelScope, SharingStarted.Lazily, initialValue)
    }
}