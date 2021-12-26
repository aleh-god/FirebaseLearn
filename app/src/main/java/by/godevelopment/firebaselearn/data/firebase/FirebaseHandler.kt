package by.godevelopment.firebaselearn.data.firebase

import android.util.Log
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.domain.model.DataState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseHandler @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) {

    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private var reference = firebaseDatabase.getReference("Users")

    fun checkCurrentUser(): Boolean = (firebaseUser != null)

//    fun signInWithEmailAndPassword(email: String, pass: String): Flow<DataState<Boolean>>
//            = callbackFlow {
//        Log.i(LOG_KEY,"signIn = $email + $pass")
////        emit(DataState.Loading())
////        delay(1000)
////        emit(DataState.Success(true))
//
//        trySend(DataState.Loading())
//        firebaseAuth.signInWithEmailAndPassword(email, pass)
//            .addOnCompleteListener { task ->
//                try {
//                    if (task.isSuccessful) trySend(DataState.Success(true))
//                    if (task.isCanceled) trySend(DataState.Success(false))
//                } catch (e: Throwable) {
//                    val error = e.message ?: "null error"
//                    DataState.Error<Boolean>(error)
//                }
//            }
//        awaitClose {
//
//        }
//    }
//
//    fun createUserWithEmailAndPassword(email: String, pass: String): Flow<DataState<Boolean>>
//            = callbackFlow {
//        Log.i(LOG_KEY,"createUser = $email + $pass")
////        emit(DataState.Loading())
////        delay(1000)
////        emit(DataState.Success(true))
//        trySend(DataState.Loading())
//        firebaseAuth.createUserWithEmailAndPassword(email, pass)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val firebaseUser = firebaseAuth.currentUser
//                    firebaseUser?.uid?.let { userId ->
//                        val reference = firebaseDatabase
//                            .getReference("Users")
//                            .child(userId)
//                        val hashMap = HashMap<String,String>()
//                        hashMap["id"] = userId
//                        hashMap["email"] = email
//
//                        reference.setValue(hashMap).addOnCompleteListener {
//                            try {
//                                if (it.isSuccessful) trySend(DataState.Success(true))
//                                if (it.isCanceled) trySend(DataState.Success(false))
//                            } catch (e: Throwable) {
//                                val error = e.message ?: "null error"
//                                DataState.Error<Boolean>(error)
//                            }
//                        }
//                    }
//                } else {
//                    trySend(DataState.Error("Кажется ты уже зарегистрирован!"))
//                }
//            }
//        awaitClose {
//
//        }
//    }

    suspend fun logInWithEmail(email: String, pass: String): AuthResult? {
        return try {
            Log.i(LOG_KEY,"logInUser = $email + $pass")
            firebaseAuth
                .signInWithEmailAndPassword(email, pass)
                .await()
        } catch (e: Exception) {
            Log.i(LOG_KEY,"logInUser = Exception")
            null
        }
    }

    suspend fun registerInFireStore(email: String, pass: String) : Boolean {
        return try {
            Log.i(LOG_KEY,"registerUser = $email + $pass")
            firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            Log.i(LOG_KEY,"registerUser = done")
            setCurrentUserInReference(email, pass)
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun setCurrentUserInReference(email: String, pass: String): Boolean {
        return try {
            Log.i(LOG_KEY,"setCurrentUserInReference = $email + $pass")
            val firebaseUser = firebaseAuth.currentUser
            firebaseUser?.uid?.let { userId ->
                Log.i(LOG_KEY,"firebaseUser?.uid?.let = $userId")
                val reference = firebaseDatabase
                    .getReference("Users")
                    .child(userId)
                val hashMap = HashMap<String, String>()
                hashMap["id"] = userId
                hashMap["email"] = email

                reference.setValue(hashMap).await()
                Log.i(LOG_KEY,"setCurrentUserInReference = true")
                return true
            }
            Log.i(LOG_KEY,"setCurrentUserInReference = false")
            false
        } catch (e: Exception) {
            false
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }
}