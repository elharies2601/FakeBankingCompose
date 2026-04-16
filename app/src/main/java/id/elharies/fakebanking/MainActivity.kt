package id.elharies.fakebanking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.elharies.fakebanking.data.repository.TransactionRepositoryImpl
import id.elharies.fakebanking.data.repository.UserRepositoryImpl
import id.elharies.fakebanking.domain.TransactionRepository
import id.elharies.fakebanking.domain.UserRepository
import id.elharies.fakebanking.navigation.BankNavigation
import id.elharies.fakebanking.ui.theme.FakeBankingTheme
import id.elharies.fakebanking.util.vmFactory.ViewModelFactory

class MainActivity : ComponentActivity() {

    private val userRepository: UserRepository by lazy(LazyThreadSafetyMode.NONE) {
        UserRepositoryImpl()
    }

    private val transactionRepository: TransactionRepository by lazy(LazyThreadSafetyMode.NONE) {
        TransactionRepositoryImpl(applicationContext)
    }

    private val vmFactory: ViewModelFactory by lazy {
        ViewModelFactory(userRepository, transactionRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FakeBankingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BankNavigation(modifier = Modifier.padding(innerPadding), vmFactory = vmFactory)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FakeBankingTheme {
        Greeting("Android")
    }
}