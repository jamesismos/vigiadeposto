package br.com.vigiadeposto.data.repository

import br.com.vigiadeposto.domain.model.Result
import br.com.vigiadeposto.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    /**
     * Login com Google
     */
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            
            result.user?.let { firebaseUser ->
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    isEmailVerified = firebaseUser.isEmailVerified
                )
                Result.Success(user)
            } ?: Result.Error(Exception("Falha na autenticação"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Login com email e senha
     */
    suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            
            result.user?.let { firebaseUser ->
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    isEmailVerified = firebaseUser.isEmailVerified
                )
                Result.Success(user)
            } ?: Result.Error(Exception("Falha na autenticação"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Registro com email e senha
     */
    suspend fun signUpWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            
            result.user?.let { firebaseUser ->
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    isEmailVerified = firebaseUser.isEmailVerified
                )
                Result.Success(user)
            } ?: Result.Error(Exception("Falha no registro"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Logout
     */
    fun signOut() {
        firebaseAuth.signOut()
    }

    /**
     * Obtém usuário atual
     */
    fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.let { firebaseUser ->
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString(),
                isEmailVerified = firebaseUser.isEmailVerified
            )
        }
    }

    /**
     * Verifica se usuário está logado
     */
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
