package id.elharies.fakebanking.ui.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.elharies.fakebanking.component.BankingButton
import id.elharies.fakebanking.data.model.user.User
import id.elharies.fakebanking.ui.theme.BlueContainer
import id.elharies.fakebanking.ui.theme.BlueDark
import id.elharies.fakebanking.ui.theme.BluePrimary
import id.elharies.fakebanking.ui.theme.GradientEnd
import id.elharies.fakebanking.ui.theme.GradientStart
import id.elharies.fakebanking.ui.theme.GrayDark
import id.elharies.fakebanking.ui.theme.GrayText
import id.elharies.fakebanking.ui.theme.RedError
import id.elharies.fakebanking.ui.theme.RedLight
import id.elharies.fakebanking.util.window.WindowWidthSize
import id.elharies.fakebanking.util.window.rememberWindowSizeInfo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (User) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is LoginEvent.NavigateToDashboard -> {
                    viewModel.processAction(LoginIntent.ClearAllField)
                    onLoginSuccess(it.user)
                }

                is LoginEvent.ShowError -> {
                    // Show error
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LoginContent(state = state) {
        viewModel.processAction(it)
    }
}

@Composable
private fun LoginContent(
    state: LoginUiState = LoginUiState(),
    onAction: (LoginIntent) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    val windowSize = rememberWindowSizeInfo()

    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd),
                        startY = 0f,
                        endY = 600f
                    )
                )
                .padding(it)
        ) {
            when (windowSize.widthSize) {
                WindowWidthSize.Expanded -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            HeaderSection(isLarge = true)
                        }
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight()
                                .padding(end = 48.dp, top = 48.dp, bottom = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CardForm(
                                modifier = Modifier,
                                state = state,
                                focusManager = focusManager,
                                passwordVisible = passwordVisible,
                                onPasswordToggle = {
                                    passwordVisible = !passwordVisible
                                },
                                onAction = onAction
                            )
                        }
                    }
                }
                WindowWidthSize.Medium -> {
                    Row(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(0.85f).padding(horizontal = 28.dp, vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            HeaderSection(isLarge = false)
                        }
                        Box(
                            modifier = Modifier.weight(1.15f)
                                .padding(end = 28.dp, top = 24.dp, bottom = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CardForm(
                                modifier = Modifier,
                                state = state,
                                focusManager = focusManager,
                                passwordVisible = passwordVisible,
                                onPasswordToggle = {
                                    passwordVisible = !passwordVisible
                                },
                                onAction = onAction
                            )
                        }
                    }
                }
                WindowWidthSize.Compact -> {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        HeaderSection()
                        Spacer(modifier = Modifier.height(32.dp))
                        // Card Form
                        CardForm(
                            modifier = Modifier,
                            state = state,
                            focusManager = focusManager,
                            passwordVisible = passwordVisible,
                            onPasswordToggle = {
                                passwordVisible = !passwordVisible
                            },
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun CardForm(
    modifier: Modifier = Modifier,
    state: LoginUiState = LoginUiState(),
    focusManager: FocusManager,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    onAction: (LoginIntent) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Masuk",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = GrayDark
            )

            // Username Field
            OutlinedTextField(
                value = state.username,
                onValueChange = { onAction(LoginIntent.UsernameChanged(it)) },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = BluePrimary)
                },
                isError = state.usernameError != null,
                supportingText = state.usernameError?.let { { Text(it, color = RedError) } },
                keyboardOptions = KeyboardOptions(
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

            // Password Field
            OutlinedTextField(
                value = state.password,
                onValueChange = { onAction(LoginIntent.PasswordChanged(it)) },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary)
                },
                trailingIcon = {
                    IconButton(onClick = onPasswordToggle) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = GrayText
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { { Text(it, color = RedError) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onAction(LoginIntent.Submit)
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

            // Error Message
            AnimatedVisibility(visible = state.loginError != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = RedLight),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = RedError,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = state.loginError ?: "",
                            color = RedError,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Login Button
            BankingButton(
                onClick = {
                    focusManager.clearFocus()
                    onAction(LoginIntent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                isLoading = state.isLoading,
                label = "Login"
            )

            // Demo hint
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = BlueContainer),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Demo Akun:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueDark
                    )
                    Text("Username: user123", fontSize = 12.sp, color = BlueDark)
                    Text("Password: password123", fontSize = 12.sp, color = BlueDark)
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    val iconSize = if (isLarge) 96.dp else 72.dp
    val iconInner = if (isLarge) 56.dp else 40.dp
    val titleSize = if (isLarge) 34.sp else 28.sp
    val corner = if (isLarge) 28.dp else 20.dp

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(corner))
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(iconInner)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Fake BankingApp",
            color = Color.White,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Selamat datang kembali",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun PreviewHeaderSection() {
    HeaderSection()
}

@Preview
@Composable
private fun PreviewLoginContent() {
    LoginContent()
}

@Preview(device = "spec:width=673dp,height=841dp,orientation=landscape")
@Composable
private fun PreviewLoginContentMedium() {
    LoginContent()
}