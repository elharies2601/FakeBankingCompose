package id.elharies.fakebanking.ui.transfer

sealed interface TransferIntent {
    data class AmountChanged(val amount: String): TransferIntent
    data class DestinationChanged(val accountNumber: String): TransferIntent
    data object SendClicked: TransferIntent
    data object ConfirmTransfer: TransferIntent
    data object DismissDialog: TransferIntent
    data object DismissResult: TransferIntent
}