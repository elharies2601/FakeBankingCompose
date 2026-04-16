package id.elharies.fakebanking.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.elharies.fakebanking.R
import id.elharies.fakebanking.component.BankingTopAppBar
import id.elharies.fakebanking.data.model.user.User
import id.elharies.fakebanking.ui.theme.BlueContainer
import id.elharies.fakebanking.ui.theme.BluePrimary
import id.elharies.fakebanking.ui.theme.GrayBackground
import id.elharies.fakebanking.ui.theme.GrayDark
import id.elharies.fakebanking.ui.theme.GrayText
import id.elharies.fakebanking.ui.theme.GreenLight
import id.elharies.fakebanking.ui.theme.GreenSuccess
import id.elharies.fakebanking.ui.theme.RedError
import id.elharies.fakebanking.ui.theme.RedLight
import id.elharies.fakebanking.util.ext.formatRupiah
import id.elharies.fakebanking.util.window.WindowWidthSize
import id.elharies.fakebanking.util.window.rememberWindowSizeInfo

@Composable
fun TransferScreen(
    viewModel: TransferViewModel = viewModel(),
    user: User? = User(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TransferContent(state = state, user = user, onBack = onBack, onAction = viewModel::processAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransferContent(
    state: TransferUiState = TransferUiState(),
    user: User? = User(),
    onBack: () -> Unit = {},
    onAction: (TransferIntent) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val windowSize = rememberWindowSizeInfo()

    val isWide = windowSize.widthSize != WindowWidthSize.Compact
    val hPad   = windowSize.horizontalPadding

    // Confirm Dialog
    if (state.showConfirmDialog) {
        ConfirmDialogScreen(onAction, state)
    }

    // Result Dialog
    state.transferResult?.let { result ->
        ResultDialog(result, onAction)
    }

    Scaffold(
        topBar = {
            BankingTopAppBar(
                title = stringResource(R.string.transfer),
                onClick = onBack
            )
        },
        containerColor = GrayBackground
    ) { padding ->
        if (state.isLoading) {
            TransferLoading(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .then(
                        if (isWide) Modifier
                            .widthIn(560.dp)
                            .fillMaxHeight() else Modifier.fillMaxSize()
                    )
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 20.dp, horizontal = hPad),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sender Info
                SenderInfoSection(user = user)

                // Form Card
                FormTransferSection(state = state, focusManager = focusManager, onAction = onAction)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onAction(TransferIntent.SendClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Icon(Icons.AutoMirrored.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.kirim), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun TransferLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = BluePrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Memproses transfer...", color = GrayText)
        }
    }
}

@Composable
private fun FormTransferSection(
    modifier: Modifier = Modifier,
    state: TransferUiState = TransferUiState(),
    focusManager: FocusManager,
    onAction: (TransferIntent) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.detail_transfer),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GrayDark
            )

            OutlinedTextField(
                value = state.destinationAccount,
                onValueChange = { onAction(TransferIntent.DestinationChanged(it)) },
                label = { Text(stringResource(R.string.nomor_rekening_tujuan)) },
                leadingIcon = {
                    Icon(Icons.Default.CreditCard, contentDescription = null, tint = BluePrimary)
                },
                supportingText = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            state.destinationError
                                ?: stringResource(R.string.masukkan_10_digit_nomor_rekening),
                            color = if (state.destinationError != null) RedError else GrayText,
                            fontSize = 12.sp
                        )
                        Text(
                            "${state.destinationAccount.length}/10",
                            fontSize = 12.sp,
                            color = GrayText
                        )
                    }
                },
                isError = state.destinationError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    focusedLabelColor = BluePrimary
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = state.amount,
                onValueChange = { onAction(TransferIntent.AmountChanged(it)) },
                label = { Text(stringResource(R.string.nominal_transfer)) },
                leadingIcon = {
                    Text(
                        "Rp",
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                supportingText = state.amountError?.let {
                    { Text(it, color = RedError, fontSize = 12.sp) }
                },
                isError = state.amountError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onAction(TransferIntent.SendClicked)
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary,
                    focusedLabelColor = BluePrimary
                ),
                singleLine = true
            )

            // Amount preview
            if (state.amount.isNotBlank() && (state.amount.toLongOrNull() ?: 0L) > 0) {
                Text(
                    text = state.amount.toDouble().formatRupiah(),
                    color = BluePrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun SenderInfoSection(modifier: Modifier = Modifier, user: User?) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlueContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = BluePrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(stringResource(R.string.rekening_asal), fontSize = 12.sp, color = GrayText)
                Text(user?.name.orEmpty(), fontWeight = FontWeight.Bold, color = GrayDark)
                Text(user?.accountNumber.orEmpty(), fontSize = 13.sp, color = GrayText)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(stringResource(R.string.saldo), fontSize = 12.sp, color = GrayText)
                Text(
                    (user?.balance ?: 0.0).formatRupiah(),
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun ResultDialog(
    result: TransferResultState,
    onAction: (TransferIntent) -> Unit = {}
) {
    Dialog(onDismissRequest = { onAction(TransferIntent.DismissResult) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val isSuccess = result is TransferResultState.Success
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(if (isSuccess) GreenLight else RedLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (isSuccess) GreenSuccess else RedError,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    text = if (isSuccess) stringResource(R.string.transfer_berhasil) else stringResource(
                        R.string.transfer_gagal
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = GrayDark
                )
                Text(
                    text = if (isSuccess)
                        stringResource(R.string.transaksi_anda_telah_berhasil_diproses)
                    else
                        (result as TransferResultState.Failed).message,
                    fontSize = 14.sp,
                    color = GrayText
                )
                Button(
                    onClick = { onAction(TransferIntent.DismissResult) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSuccess) GreenSuccess else BluePrimary
                    )
                ) { Text("OK") }
            }
        }
    }
}

@Composable
private fun ConfirmDialogScreen(
    onAction: (TransferIntent) -> Unit,
    state: TransferUiState
) {
    AlertDialog(
        onDismissRequest = { onAction(TransferIntent.DismissDialog) },
        title = {
            Text(stringResource(R.string.konfirmasi_transfer), fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ConfirmRow(
                    label = stringResource(R.string.ke_rekening),
                    value = state.destinationAccount
                )
                ConfirmRow(
                    label = stringResource(R.string.nominal),
                    value = (state.amount.toDoubleOrNull() ?: 0.0).formatRupiah()
                )
                HorizontalDivider()
                Text(
                    text = stringResource(R.string.pastikan_data_sudah_benar_sebelum_melanjutkan),
                    fontSize = 13.sp,
                    color = GrayText
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAction(TransferIntent.ConfirmTransfer) },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(10.dp)
            ) { Text(stringResource(R.string.konfirmasi)) }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onAction(TransferIntent.DismissDialog) },
                shape = RoundedCornerShape(10.dp)
            ) { Text(stringResource(R.string.batal)) }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ConfirmRow(modifier: Modifier = Modifier, label: String, value: String) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = GrayText, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.SemiBold, color = GrayDark, fontSize = 14.sp)
    }
}

@Preview
@Composable
private fun PreviewFormTransferSection() {
    FormTransferSection(focusManager = LocalFocusManager.current)
}

@Preview
@Composable
private fun PreviewSenderInfoSection() {
    SenderInfoSection(user = User(name = "Elharies", accountNumber = "1234567890"))
}

@Preview
@Composable
private fun PreviewConfirmRow() {
    ConfirmRow(label = "Ke Rekening", value = "1234567890")
}

@Preview
@Composable
private fun PreviewTransferContent() {
    TransferContent()
}

@Preview(device = "spec:width=673dp,height=841dp")
@Composable
private fun PreviewTransferNonCompatContent() {
    TransferContent()
}