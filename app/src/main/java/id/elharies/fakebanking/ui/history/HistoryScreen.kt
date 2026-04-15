package id.elharies.fakebanking.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.elharies.fakebanking.R
import id.elharies.fakebanking.component.BankingTopAppBar
import id.elharies.fakebanking.data.model.transaction.TransactionRes
import id.elharies.fakebanking.ui.theme.BluePrimary
import id.elharies.fakebanking.ui.theme.GrayBackground
import id.elharies.fakebanking.ui.theme.GrayDark
import id.elharies.fakebanking.ui.theme.GrayText
import id.elharies.fakebanking.ui.theme.GreenLight
import id.elharies.fakebanking.ui.theme.GreenSuccess
import id.elharies.fakebanking.ui.theme.RedError
import id.elharies.fakebanking.ui.theme.RedLight
import id.elharies.fakebanking.util.constants.TransactionStatus
import id.elharies.fakebanking.util.ext.formatRupiah
import id.elharies.fakebanking.util.ext.toDate
import id.elharies.fakebanking.util.ext.toString
import id.elharies.fakebanking.util.window.WindowWidthSize
import id.elharies.fakebanking.util.window.rememberWindowSizeInfo

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1= Unit) {
        viewModel.processAction(HistoryIntent.LoadHistory)
    }

    HistoryContent(state = state, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryContent(
    state: HistoryUiState = HistoryUiState(),
    onBack: () -> Unit = {}
) {
    val windowSize = rememberWindowSizeInfo()

    Scaffold(
        topBar = {
            BankingTopAppBar(
                title = stringResource(R.string.riwayat_transaksi),
                onClick = onBack
            )
        },
        containerColor = GrayBackground
    ) { padding ->
        if (state.isLoading) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else if (state.transactions.isEmpty()) {
            EmptyScreen(modifier = Modifier.padding(padding))
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                // Summary Header
                SummaryHeaderSection(state = state)

                if (windowSize.widthSize == WindowWidthSize.Expanded) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = windowSize.horizontalPadding, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = state.transactions, key = { it.id }) {
                            TransactionItem(transaction = it)
                        }
                        item { Spacer(Modifier.height(8.dp)) }
                        item { Spacer(Modifier.height(8.dp)) }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = windowSize.horizontalPadding, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(items = state.transactions, key = { it.id }) {
                            TransactionItem(transaction = it)
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryHeaderSection(modifier: Modifier = Modifier, state: HistoryUiState) {
    val windowSize = rememberWindowSizeInfo()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = windowSize.horizontalPadding),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BluePrimary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    stringResource(R.string.total_transaksi),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    "${state.transactions.size} Transaksi",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val successCount =
                    state.transactions.count { it.status == TransactionStatus.SUCCESS }
                val failedCount = state.transactions.count { it.status == TransactionStatus.FAILED }
                StatusBadge(label = "Sukses", count = successCount, color = GreenSuccess)
                StatusBadge(label = "Gagal", count = failedCount, color = RedError)
            }
        }
    }
}

@Composable
private fun EmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.AutoMirrored.Default.ReceiptLong,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(stringResource(R.string.belum_ada_transaksi), color = GrayText)
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp), color = BluePrimary)
    }
}

@Composable
private fun StatusBadge(
    modifier: Modifier = Modifier,
    label: String, count: Int, color: Color = Color.White
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count.toString(), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = color.copy(alpha = 0.8f), fontSize = 11.sp)
    }
}

@Composable
private fun TransactionItem(modifier: Modifier = Modifier, transaction: TransactionRes) {
    val isSuccess = transaction.status == TransactionStatus.SUCCESS

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isSuccess) GreenLight else RedLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSuccess) Icons.Default.ArrowUpward else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isSuccess) GreenSuccess else RedError,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    fontWeight = FontWeight.SemiBold,
                    color = GrayDark,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.accountNumber,
                    color = GrayText,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = GrayText,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = transaction.date.toDate("yyyy-MM-dd").toString("dd MMM yyyy"),
                        color = GrayText,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "- ${transaction.amount.formatRupiah()}",
                    fontWeight = FontWeight.Bold,
                    color = if (isSuccess) GrayDark else RedError,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSuccess) GreenLight else RedLight
                ) {
                    Text(
                        text = if (isSuccess) "Sukses" else "Gagal",
                        color = if (isSuccess) GreenSuccess else RedError,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTransactionItem() {
    val transaction = TransactionRes(
        id = "TRX-1",
        date = "2024-01-12",
        amount = 3000000.0,
        description = "Transfer ke PT Maju Jaya",
        accountNumber = "5544332211",
        status = "SUCCESS"
    )
    TransactionItem(transaction = transaction)
}

@Preview
@Composable
private fun PreviewStatusBadge() {
    StatusBadge(label = "Berhasil", count = 1)
}

@Preview
@Composable
private fun PreviewHistoryContent() {
    val state = HistoryUiState(transactions = listOf(
        TransactionRes(
            id = "TRX-1",
            date = "2024-01-12",
            amount = 3000000.0,
            description = "Transfer ke PT Maju Jaya",
            accountNumber = "5544332211",
            status = "SUCCESS"
        ),
        TransactionRes(
            id = "TRX-2",
            date = "2024-01-12",
            amount = 3000000.0,
            description = "Transfer ke PT Maju Jaya",
            accountNumber = "5544332211",
            status = "SUCCESS"
        )
    ))
    HistoryContent(state = state)
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewHistoryTabletContent() {
    val state = HistoryUiState(transactions = listOf(
        TransactionRes(
            id = "TRX-1",
            date = "2024-01-12",
            amount = 3000000.0,
            description = "Transfer ke PT Maju Jaya",
            accountNumber = "5544332211",
            status = "SUCCESS"
        ),
        TransactionRes(
            id = "TRX-2",
            date = "2024-01-12",
            amount = 3000000.0,
            description = "Transfer ke PT Maju Jaya",
            accountNumber = "5544332211",
            status = "SUCCESS"
        )
    ))
    HistoryContent(state = state)
}