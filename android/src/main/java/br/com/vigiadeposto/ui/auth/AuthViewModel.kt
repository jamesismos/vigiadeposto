package br.com.vigiadeposto.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vigiadeposto.data.repository.AuthRepository
import br.com.vigiadeposto.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    _authState.value = AuthState.Success(currentUser)
                } else {
                    _authState.value = AuthState.Idle
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro ao verificar autenticação: ${e.message}")
            }
        }
    }

    fun signInWithGoogle(account: com.google.android.gms.auth.api.signin.GoogleSignInAccount) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            try {
                val result = authRepository.signInWithGoogle(account)
                when (result) {
                    is br.com.vigiadeposto.domain.model.Result.Success -> {
                        _authState.value = AuthState.Success(result.data)
                    }
                    is br.com.vigiadeposto.domain.model.Result.Error -> {
                        _authState.value = AuthState.Error("Falha no login com Google: ${result.exception.message}")
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro inesperado: ${e.message}")
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            try {
                val result = authRepository.signInWithEmail(email, password)
                when (result) {
                    is br.com.vigiadeposto.domain.model.Result.Success -> {
                        _authState.value = AuthState.Success(result.data)
                    }
                    is br.com.vigiadeposto.domain.model.Result.Error -> {
                        _authState.value = AuthState.Error(getErrorMessage(result.exception))
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro inesperado: ${e.message}")
            }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            try {
                val result = authRepository.signUpWithEmail(email, password)
                when (result) {
                    is br.com.vigiadeposto.domain.model.Result.Success -> {
                        _authState.value = AuthState.Success(result.data)
                    }
                    is br.com.vigiadeposto.domain.model.Result.Error -> {
                        _authState.value = AuthState.Error(getErrorMessage(result.exception))
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro inesperado: ${e.message}")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState.value = AuthState.Idle
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }

    private fun getErrorMessage(exception: Exception): String {
        return when {
            exception.message?.contains("password") == true -> "Senha incorreta"
            exception.message?.contains("email") == true -> "Email inválido"
            exception.message?.contains("network") == true -> "Erro de conexão"
            exception.message?.contains("user-not-found") == true -> "Usuário não encontrado"
            exception.message?.contains("weak-password") == true -> "Senha muito fraca"
            exception.message?.contains("email-already-in-use") == true -> "Email já está em uso"
            else -> "Erro de autenticação: ${exception.message}"
        }
    }
}
