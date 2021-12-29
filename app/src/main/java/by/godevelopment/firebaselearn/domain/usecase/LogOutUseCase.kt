package by.godevelopment.firebaselearn.domain.usecase

import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) {
    operator fun invoke() {
        firebaseHandler.logOut()
    }
}