package by.godevelopment.firebaselearn.data.firebase

import android.util.Log
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.domain.model.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FirebaseHandler @Inject constructor(
//    private val firebaseAuth: FirebaseAuth,
//    private val firebaseDatabase: FirebaseDatabase
) {

//    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
//    private var reference: DatabaseReference? = firebaseDatabase.reference

//    fun checkCurrentUser(): Boolean = (firebaseUser != null)

    fun signInWithEmailAndPassword(email: String, pass: String): Flow<DataState<Boolean>> = flow {
        Log.i(LOG_KEY,"signIn = $email + $pass")
        emit(DataState.Loading())
        delay(1000)
        emit(DataState.Success(true))

//        trySend(DataState.Loading())
//        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
//            try {
//                if (task.isSuccessful) trySend(DataState.Success(true))
//                if (task.isCanceled) trySend(DataState.Success(false))
//            } catch (e: Throwable) {
//                val error = e.message ?: "null error"
//                DataState.Error<Boolean>(error)
//            }
//        }
    }

    fun createUserWithEmailAndPassword(email: String, pass: String): Flow<DataState<Boolean>> = flow {
        Log.i(LOG_KEY,"createUser = $email + $pass")
        emit(DataState.Loading())
        delay(100)
        emit(DataState.Success(true))
//        trySend(DataState.Loading())
//        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val firebaseUser = firebaseAuth.currentUser
//                val userId = firebaseUser?.uid
//                val referenceAny = firebaseDatabase.reference.child(userId)
//            }
//        }
    }
}