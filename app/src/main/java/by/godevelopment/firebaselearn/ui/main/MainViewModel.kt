package by.godevelopment.firebaselearn.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import by.godevelopment.firebaselearn.domain.model.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) : ViewModel() {

    // input Flow
    val userState =  MutableStateFlow(UserState())
    private val onClickLoginEvent =  MutableStateFlow(false)

    // output Flow
    private val _eventState =  MutableStateFlow(EventState())
    val eventState: StateFlow<EventState> = _eventState

    // для прогресс бара
    val isLoading =  MutableStateFlow(false)

    val isEnableBtnLogin: StateFlow<Boolean> = userState.map {
        if (it.userEmail.isEmpty() || it.userPassword.isEmpty()) {
            _eventState.value = EventState(
                alertMessage = "Поля пусты!"
            )
            false
        } else {
            true
        }
    }.toStateFlow(false)

    init {
        viewModelScope.launch {
            onClickLoginEvent.collect {
                if (it) {
                    firebaseHandler.signInWithEmailAndPassword(
                        userState.value.userEmail,
                        userState.value.userPassword
                    ).collect { state ->
                        when (state) {
                            is DataState.Loading -> {
                                isLoading.value = true
                            }
                            is DataState.Success -> {
                                _eventState.value = EventState(runNavHome = true)
                                isLoading.value = false
                            }
                            is DataState.Error -> {
                                _eventState.value = EventState(
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
        _eventState.value = EventState(
            runNavReg = true
        )
    }

    /** Convenience method to transform a [Flow] to a [StateFlow]. */
    private fun <T> Flow<T>.toStateFlow(initialValue: T): StateFlow<T> {
        return this.stateIn(viewModelScope, SharingStarted.Lazily, initialValue)
    }

    data class UserState(
        var userEmail: String = "",
        var userPassword: String = "",
    )

    // Здесь нужно решение что бы евент отработал один раз
    data class EventState(
        var alertMessage: String? = null,
        var runNavHome: Boolean = false,
        var runNavReg: Boolean = false
    )
}