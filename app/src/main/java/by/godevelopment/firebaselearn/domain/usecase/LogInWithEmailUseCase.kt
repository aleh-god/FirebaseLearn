package by.godevelopment.firebaselearn.domain.usecase

import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import javax.inject.Inject

class LogInWithEmailUseCase @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) {
    suspend operator fun invoke(email: String, password: String) {
        firebaseHandler.logInWithEmail(email, password)
    }
}