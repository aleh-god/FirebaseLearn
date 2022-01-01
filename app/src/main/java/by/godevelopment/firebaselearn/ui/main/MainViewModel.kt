package by.godevelopment.firebaselearn.ui.main

import android.provider.Settings.System.getString
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.common.AuthException
import by.godevelopment.firebaselearn.common.EMPTY_STRING_VALUE
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.domain.helpers.StringHelper
import by.godevelopment.firebaselearn.domain.model.EventState
import by.godevelopment.firebaselearn.domain.usecase.CheckCurrentUserUseCase
import by.godevelopment.firebaselearn.domain.usecase.LogInWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logInWithEmailUseCase: LogInWithEmailUseCase,
    private val checkCurrentUserUseCase: CheckCurrentUserUseCase,
    private val stringHelper: StringHelper
) : ViewModel() {
    // input Flow
    val email =  MutableStateFlow(EMPTY_STRING_VALUE)
    val password =  MutableStateFlow(EMPTY_STRING_VALUE)

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
            _eventState.value = EventState.Alert(stringHelper.getString(R.string.alert_pull_fields))
            false
        } else {
            _eventState.value = EventState.Hold
            true
        }
    }

    init {
        Log.i(LOG_KEY, "MainViewModel init")
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
                logInWithEmailUseCase(
                    email.value,
                    password.value
                )
                _eventState.value = EventState.RunNav(R.id.action_main_fragment_to_home_fragment)
            } catch (e: AuthException) {
                _eventState.value = EventState.Alert(
                    stringHelper.getString(R.string.alert_auth_server_trouble)
                )
            } finally {
                _isLoadingProgBar.value = false
                _isEnableBtnLogin.value = true
            }
        }
    }

    fun onClickReg() {
            Log.i(LOG_KEY, "MainViewModel onClickReg()")
            _eventState.value = EventState.RunNav(R.id.action_main_fragment_to_register_fragment)
    }
    
    fun checkCurrentUser(): Boolean = checkCurrentUserUseCase()

    fun resetEventState() {
        _eventState.value = EventState.Hold
    }
}
