package id.elharies.fakebanking.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import id.elharies.fakebanking.R
import id.elharies.fakebanking.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankingTopAppBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.riwayat_transaksi),
    onClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title, fontWeight = FontWeight.Bold, color = Color.White) },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
    )
}

@Preview(name = "BankingTopAppBar")
@Composable
private fun PreviewBankingTopAppBar() {
    BankingTopAppBar()
}