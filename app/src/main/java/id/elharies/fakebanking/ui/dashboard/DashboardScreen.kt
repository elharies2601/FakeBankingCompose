package id.elharies.fakebanking.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.SendToMobile
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.elharies.fakebanking.R
import id.elharies.fakebanking.data.model.user.User
import id.elharies.fakebanking.ui.theme.BlueContainer
import id.elharies.fakebanking.ui.theme.BlueDark
import id.elharies.fakebanking.ui.theme.BluePrimary
import id.elharies.fakebanking.ui.theme.GrayBackground
import id.elharies.fakebanking.ui.theme.GrayDark
import id.elharies.fakebanking.ui.theme.GrayText
import id.elharies.fakebanking.ui.theme.Purple40
import id.elharies.fakebanking.util.ext.formatRupiah
import id.elharies.fakebanking.util.window.WindowWidthSize
import id.elharies.fakebanking.util.window.rememberWindowSizeInfo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    user: User? = User(),
    oNavigateToTransfer: () -> Unit = {},
    oNavigateToHistory: () -> Unit = {},
    oNavigateToLogin: () -> Unit = {}
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                DashboardEvent.NavigateToHistory -> oNavigateToHistory()
                DashboardEvent.NavigateToLogin -> oNavigateToLogin()
                DashboardEvent.NavigateToTransfer -> oNavigateToTransfer()
            }
        }
    }

    DashboardContent(user = user) { intent -> viewModel.processAction(intent) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    user: User? = User(),
    onAction: (DashboardIntent) -> Unit = {}
) {
    val windowSize = rememberWindowSizeInfo()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = { onAction(DashboardIntent.LogoutClicked) }) {
                        Icon(
                            Icons.AutoMirrored.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        },
        containerColor = GrayBackground
    ) { padding ->

        when (windowSize.widthSize) {
            WindowWidthSize.Expanded -> {
                Row(
                    modifier = Modifier.fillMaxSize().padding(padding)
                ) {
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        // Balance Card
                        BalanceSection(
                            modifier = Modifier
                                .fillMaxHeight(), user = user
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Menu Section
                        MenuSection(modifier = Modifier.weight(1f), onAction = onAction)

//                        Spacer(modifier = Modifier.height(24.dp))

                        // Info Card
                        InfoBannerSection()
                    }
                }
            }
            WindowWidthSize.Medium -> {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)) {
                    // Balance Card
                    BalanceSection(
                        modifier = Modifier
                            .width(240.dp)
                            .fillMaxHeight(), user = user
                    )
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Menu Section
                        MenuSection(onAction = onAction)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Info Card
                        InfoBannerSection()
                    }
                }
            }

            WindowWidthSize.Compact -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Balance Card
                    BalanceSection(user = user)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Menu Section
                    MenuSection(onAction = onAction)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Info Card
                    InfoBannerSection()

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

    }
}

@Composable
private fun MenuSection(modifier: Modifier = Modifier, onAction: (DashboardIntent) -> Unit = {}) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.menu_layanan),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = GrayDark,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuCard(
                icon = Icons.AutoMirrored.Default.SendToMobile,
                title = stringResource(R.string.transfer),
                subtitle = stringResource(R.string.kirim_uang),
                color = BluePrimary,
                modifier = Modifier.weight(1f),
                onClick = { onAction(DashboardIntent.TransferClicked) }
            )
            MenuCard(
                icon = Icons.Default.Receipt,
                title = stringResource(R.string.riwayat),
                subtitle = stringResource(R.string.transaksi),
                color = Purple40,
                modifier = Modifier.weight(1f),
                onClick = { onAction(DashboardIntent.HistoryClicked) }
            )
        }
    }
}

@Composable
private fun InfoBannerSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlueContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Security,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.transaksi_aman),
                    fontWeight = FontWeight.SemiBold,
                    color = BlueDark,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.semua_transaksi_dilindungi_enkripsi_256_bit),
                    color = GrayText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun BalanceSection(modifier: Modifier = Modifier, user: User?) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BluePrimary, BlueDark)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.selamat_datang),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = user?.name ?: "",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.saldo_rekening),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = (user?.balance ?: 0.0).formatRupiah(),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No. Rek: ${user?.accountNumber}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun MenuCard(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Receipt,
    title: String = "",
    subtitle: String = "",
    color: Color = Color(0xFF7C3AED),
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GrayDark)
            Text(text = subtitle, fontSize = 12.sp, color = GrayText)
        }
    }
}

@Preview
@Composable
private fun PreviewMenuSection() {
    MenuSection()
}

@Preview
@Composable
private fun PreviewInfoBannerSection() {
    InfoBannerSection()
}

@Preview
@Composable
private fun PreviewBalanceSection() {
    BalanceSection(user = User("nama", "namaa", 288_000_000_000.0, "1234567"))
}

@Preview
@Composable
private fun PreviewMenuCard() {
    MenuCard()
}

@Preview
@Composable
private fun PreviewDashboardContent() {
    DashboardContent()
}

@Preview(device = "spec:width=673dp,height=841dp")
@Composable
private fun PreviewDashboardLandscapeMediumContent() {
    DashboardContent()
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDashboardExpandedContent() {
    DashboardContent()
}
