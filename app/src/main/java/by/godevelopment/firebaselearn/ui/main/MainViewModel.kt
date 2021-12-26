package by.godevelopment.firebaselearn.ui.main

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
class MainViewModel @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) : ViewModel() {

    // input Flow
    val email =  MutableStateFlow("")
    val password =  MutableStateFlow("")
    private val onClickLoginEvent =  MutableStateFlow(false)

    // output Flow
    private val _eventState: MutableStateFlow<EventState> =  MutableStateFlow(EventState.Hold)
    val eventState: StateFlow<EventState> = _eventState

    val isLoading =  MutableStateFlow(false)

    val isEnableBtnLogin: StateFlow<Boolean> = combine(email, password) {
        email, pass ->
        Log.i(LOG_KEY, "MainViewModel userState.map $email + $pass")
        if (email.isNullOrEmpty() && pass.isNullOrEmpty()) {
            Log.i(LOG_KEY, "MainViewModel EventState.Alert create")
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
            onClickLoginEvent.collect {
                Log.i(LOG_KEY, "MainViewModel onClickLoginEvent.collect $it")
                if (it) {
                    firebaseHandler.signInWithEmailAndPassword(
                        email.value,
                        password.value
                    ).collect { state ->
                        Log.i(LOG_KEY, "MainViewModel signInWithEmailAndPassword.collect $state")
                        when (state) {
                            is DataState.Loading -> {
                                isLoading.value = true
                            }
                            is DataState.Success -> {
                                _eventState.value = EventState.RunNav(R.id.action_main_fragment_to_homeFragment)
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

    fun onClickLogin() {
        Log.i(LOG_KEY, "MainViewModel onClickLogin()")
        onClickLoginEvent.value = true
    }

    fun onClickReg() {
        Log.i(LOG_KEY, "MainViewModel onClickReg()")
        _eventState.value = EventState.RunNav(R.id.action_main_fragment_to_registerFragment)
    }
    
    fun checkCurrentUser(): Boolean = firebaseHandler.checkCurrentUser()

    /** Convenience method to transform a [Flow] to a [StateFlow]. */
    private fun <T> Flow<T>.toStateFlow(initialValue: T): StateFlow<T> {
        return this.stateIn(viewModelScope, SharingStarted.Lazily, initialValue)
    }
//
//    data class UserState(
//        var userEmail: String = "",
//        var userPassword: String = "",
//    )
}