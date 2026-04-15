package id.elharies.fakebanking.ui.transfer

sealed interface TransferResultState {
    data object Success: TransferResultState
    data class Failed(val message: String): TransferResultState
}