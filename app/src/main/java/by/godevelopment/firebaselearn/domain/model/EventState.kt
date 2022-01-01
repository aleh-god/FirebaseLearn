package by.godevelopment.firebaselearn.domain.model

import androidx.annotation.IdRes

sealed class EventState {
    object Hold: EventState()
    data class RunNav(@IdRes var destination: Int): EventState()
    data class Alert(var alertMessage: String): EventState()
}