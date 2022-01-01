package by.godevelopment.firebaselearn.domain.usecase

import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import javax.inject.Inject

class CheckCurrentUserUseCase @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) {
    operator fun invoke(): Boolean = firebaseHandler.checkCurrentUser()
}