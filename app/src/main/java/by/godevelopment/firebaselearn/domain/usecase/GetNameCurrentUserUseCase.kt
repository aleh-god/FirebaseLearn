package by.godevelopment.firebaselearn.domain.usecase

import by.godevelopment.firebaselearn.data.firebase.FirebaseHandler
import javax.inject.Inject

class GetNameCurrentUserUseCase @Inject constructor(
    private val firebaseHandler: FirebaseHandler
) {
    suspend operator fun invoke(): String {
        return firebaseHandler.getNameCurrentUser()
    }
}
