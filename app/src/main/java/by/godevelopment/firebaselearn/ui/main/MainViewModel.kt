package by.godevelopment.firebaselearn.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.common.AuthException
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
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

    // output Flow
    private val _eventState: MutableStateFlow<EventState> =  MutableStateFlow(EventState.Hold)
    val eventState: StateFlow<EventState> = _eventState
    private val _isLoadingProgBar =  MutableStateFlow(false)
    val isLoadingProgBar: StateFlow<Boolean> = _isLoadingProgBar
    private val _isEnableBtnLogin = MutableStateFlow(false)
    val isEnableBtnLogin: StateFlow<Boolean> = _isEnableBtnLogin

    private val checkFields = combine(email, password) {
        email, pass ->
        Log.i(LOG_KEY, "MainViewModel userState.map $email + $pass")
        val check = email.isNotBlank() && pass.isNotBlank()
        if (!check) {
            _eventState.value = EventState.Alert("Заполните все поля!")
            delay(300)
            _eventState.value = EventState.Hold
            false
        } else true
    }

    init {
        viewModelScope.launch {
            checkFields.collect {
                Log.i(LOG_KEY, "MainViewModel onClickLoginEvent.collect $it")
                _isEnableBtnLogin.value = it
                }
            }
        }

    fun onClickLogin() {
        Log.i(LOG_KEY, "MainViewModel onClickLogin()")
        viewModelScope.launch {
            _isLoadingProgBar.value = true
            _isEnableBtnLogin.value = false
            try {
                firebaseHandler.logInWithEmail(
                    email.value,
                    password.value
                )
                _eventState.value = EventState.RunNav(R.id.action_main_fragment_to_homeFragment)
            } catch (e: AuthException) {
                _eventState.value = EventState.Alert("Проблема с сервером авторизации!")
            } finally {
                _isLoadingProgBar.value = false
                _isEnableBtnLogin.value = true
            }
        }
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
}
