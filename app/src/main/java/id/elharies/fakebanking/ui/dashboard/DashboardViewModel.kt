package id.elharies.fakebanking.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.elharies.fakebanking.data.model.user.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(): ViewModel() {
    private val _event: MutableSharedFlow<DashboardEvent> = MutableSharedFlow(extraBufferCapacity = 1)
    val event: SharedFlow<DashboardEvent>
        get() = _event.asSharedFlow()

    fun processAction(intent: DashboardIntent) {
        viewModelScope.launch {
            when(intent) {
                DashboardIntent.HistoryClicked -> _event.emit(DashboardEvent.NavigateToHistory)
                DashboardIntent.LogoutClicked -> _event.emit(DashboardEvent.NavigateToLogin)
                DashboardIntent.TransferClicked -> _event.emit(DashboardEvent.NavigateToTransfer)
            }
        }
    }
}