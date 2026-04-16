package id.elharies.fakebanking.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.elharies.fakebanking.data.model.result.ApiResult
import id.elharies.fakebanking.data.model.transaction.TransferReq
import id.elharies.fakebanking.domain.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferViewModel(private val transactionRepository: TransactionRepository) :
    ViewModel() {

    private val _state: MutableStateFlow<TransferUiState> = MutableStateFlow(TransferUiState())
    val state: StateFlow<TransferUiState>
        get() = _state.asStateFlow()

    fun processAction(intent: TransferIntent) {
        when(intent) {
            is TransferIntent.DestinationChanged -> {
                val filtered = intent.accountNumber.filter { it.isDigit() }.take(10)
                _state.update { it.copy(destinationAccount = filtered, destinationError = null) }
            }
            is TransferIntent.AmountChanged -> {
                val filtered = intent.amount.filter { it.isDigit() }
                _state.update { it.copy(amount = filtered, amountError = null) }
            }
            TransferIntent.SendClicked -> validateAndShowConfirm()
            TransferIntent.ConfirmTransfer -> {
                executeTransfer()
            }
            TransferIntent.DismissDialog -> {
                _state.update { it.copy(showConfirmDialog = false) }
            }
            TransferIntent.DismissResult -> {
                _state.update { it.copy(transferResult = null) }
            }
        }
    }

    private fun validateAndShowConfirm() {
        val st = _state.value
        val destError = when {
            st.destinationAccount.isBlank() -> "Nomor rekening tidak boleh kosong"
            st.destinationAccount.length != 10 -> "Nomor rekening harus 10 digit"
            else -> null
        }
        val amountVal = st.amount.toLongOrNull() ?: 0L
        val amountError = when {
            st.amount.isBlank() -> "Nominal tidak boleh kosong"
            amountVal <= 0 -> "Nominal harus lebih dari 0"
            else -> null
        }

        if (destError != null || amountError != null) {
            _state.update { it.copy(destinationError = destError, amountError = amountError) }
            return
        }
        _state.update { it.copy(showConfirmDialog = true) }
    }

    private fun executeTransfer() {
        val st = _state.value
        _state.update { it.copy(showConfirmDialog = false, isLoading = true) }

        viewModelScope.launch {
            val req = TransferReq(st.destinationAccount,
                st.amount.toDouble())
            when (val result = transactionRepository.transfer(req)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transferResult = TransferResultState.Success,
                            destinationAccount = "",
                            amount = ""
                        )
                    }
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transferResult = TransferResultState.Failed(result.message)
                        )
                    }
                }
            }
        }
    }
}