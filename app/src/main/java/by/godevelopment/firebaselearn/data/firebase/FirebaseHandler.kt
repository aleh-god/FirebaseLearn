package by.godevelopment.firebaselearn.data.firebase

import android.util.Log
import by.godevelopment.firebaselearn.common.AuthException
import by.godevelopment.firebaselearn.common.LOG_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FirebaseHandler @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {
    fun checkCurrentUser(): Boolean {
        return try {
            firebaseAuth.currentUser != null
        } catch (e: Exception) {
            throw AuthException()
        }
    }

    suspend fun getNameCurrentUser(): String = withContext(ioDispatcher) {
        try {
            firebaseAuth.currentUser?.email ?: "anon"
        } catch (e: Exception) {
            throw AuthException()
        }
    }

    suspend fun logInWithEmail(email: String, pass: String): Unit
    = withContext(ioDispatcher) {
        try {
            Log.i(LOG_KEY,"logInUser = $email + $pass")
            firebaseAuth
                .signInWithEmailAndPassword(email, pass)
                .await()
        } catch (e: Exception) {
            Log.i(LOG_KEY,"logInUser = Exception")
            throw AuthException()
        }
    }

    suspend fun registerInFireStore(email: String, pass: String): Unit
    = withContext(ioDispatcher) {
        try {
            Log.i(LOG_KEY,"registerInFireStore = $email + $pass")
            firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            Log.i(LOG_KEY,"registerInFireStore = done")
            setCurrentUserInReference(email, pass)
            Log.i(LOG_KEY,"registerInFireStore = end")
        } catch (e: Exception) {
            throw AuthException()
        }
    }

    private suspend fun setCurrentUserInReference(email: String, pass: String): Unit
    = withContext(ioDispatcher) {
        try {
            Log.i(LOG_KEY,"setCurrentUserInReference = $email + $pass")
            val firebaseUser = firebaseAuth.currentUser
            firebaseUser?.uid?.let { userId ->
                Log.i(LOG_KEY,"setCurrentUserInReference.uid?.let = $userId")
                val reference = firebaseDatabase
                    .getReference("Users")
                    .child(userId)
                val hashMap = HashMap<String, String>()
                hashMap["id"] = userId
                hashMap["email"] = email
                reference.setValue(hashMap).await()
                Log.i(LOG_KEY,"setCurrentUserInReference = done")
            }
            Log.i(LOG_KEY,"setCurrentUserInReference = end")
        } catch (e: Exception) {
            throw AuthException()
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }
}
