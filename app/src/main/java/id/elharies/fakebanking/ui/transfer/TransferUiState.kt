package id.elharies.fakebanking.ui.transfer

data class TransferUiState(
    val destinationAccount: String = "",
    val amount: String = "",
    val destinationError: String? = null,
    val amountError: String? = null,
    val isLoading: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val transferResult: TransferResultState? = null
)
