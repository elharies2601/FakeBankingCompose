package id.elharies.fakebanking.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.domain.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {
    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState>
        get() = _state.asStateFlow()

    private val _event: MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val event: SharedFlow<LoginEvent>
        get() = _event.asSharedFlow()

    fun processAction(action: LoginIntent) {
        when (action) {
            LoginIntent.ClearError -> {
                _state.update { it.copy(loginError = null) }
            }
            LoginIntent.Submit -> {
                handleLogin()
            }
            is LoginIntent.PasswordChanged -> {
                _state.update { it.copy(password = action.password, passwordError = null, loginError = null) }
            }
            is LoginIntent.UsernameChanged -> {
                _state.update { it.copy(username = action.username, usernameError = null, loginError = null) }
            }

            LoginIntent.ClearAllField -> {
                _state.update { it.copy(username = "", password = "", usernameError = null, passwordError = null, loginError = null) }
            }
        }

    }

    private fun handleLogin() {
        val currentState = _state.value

        val usernameError = when {
            currentState.username.isBlank() -> "Username tidak boleh kosong"
            else -> null
        }
        val passwordError = when {
            currentState.password.isBlank() -> "Password tidak boleh kosong"
            currentState.password.length < 6 -> "Password minimal 6 karakter"
            else -> null
        }

        if (usernameError != null || passwordError != null) {
            _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loginError = null) }
            when (val result = repository.login(currentState.username, currentState.password)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(LoginEvent.NavigateToDashboard(result.result))
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, loginError = result.message) }
                    _event.emit(LoginEvent.ShowError(result.message))
                }
            }
        }
    }
}