package id.elharies.fakebanking.ui.transfer

sealed interface TransferEvent {
    data object NavigateBack: TransferEvent
}