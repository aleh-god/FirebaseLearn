package by.godevelopment.firebaselearn.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.common.AuthException
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import by.godevelopment.firebaselearn.domain.model.EventState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // output Flow
    private val _eventState: MutableStateFlow<EventState> =  MutableStateFlow(EventState.Hold)
    val eventState: StateFlow<EventState> = _eventState
    private val _isLoadingProgBar =  MutableStateFlow(false)
    val isLoadingProgBar: StateFlow<Boolean> = _isLoadingProgBar
    private val _isEnableBtnReg = MutableStateFlow(false)
    val isEnableBtnReg: StateFlow<Boolean> = _isEnableBtnReg


    private val checkFields = combine(email, password) {
            email, pass ->
        Log.i(LOG_KEY, "RegisterViewModel userState.map $email + $pass")
        val check = email.isNotBlank() && pass.isNotBlank()
        if (!check) {
            _eventState.value = EventState.Alert("Заполните все поля!")
            false
        } else if (pass.length < 6) {
            _eventState.value = EventState.Alert("Пароль должен содержать, как минимум 6 символов!")
            false
        } else {
            _eventState.value = EventState.Hold
            true
        }
    }

    init {
        viewModelScope.launch {
            checkFields.collect {
                Log.i(LOG_KEY, "RegisterViewModel onClickLoginEvent.collect $it")
                _isEnableBtnReg.value = it
            }
        }
    }

    fun onClickReg() {
        Log.i(LOG_KEY, "RegisterViewModel onClickReg()")
        viewModelScope.launch {
            _isLoadingProgBar.value = true
            _isEnableBtnReg.value = false
            try {
                firebaseHandler.registerInFireStore(
                    email.value,
                    password.value
                )
                _eventState.value = EventState.RunNav(R.id.action_register_fragment_to_main_fragment)
            } catch (e: AuthException) {
                _eventState.value = EventState.Alert("Проблема с сервером авторизации!")
            } finally {
                _isLoadingProgBar.value = false
                _isEnableBtnReg.value = true
            }
        }
    }

    fun onClickBack() {
        Log.i(LOG_KEY, "RegisterViewModel onClickBack()")
        _eventState.value = EventState.RunNav(R.id.action_register_fragment_to_main_fragment)
    }
}