package moi.choucroutemelba.thepronoteclient.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.viewinterop.AndroidView
import moi.choucroutemelba.thepronoteclient.data.pronote.PronoteWebViewClient

@Composable
fun LoginRoute(
    loginViewModel: LoginViewModel,
) {

    LoginScreen(
        uiState = loginViewModel.uiState,
        onNavToHome = loginViewModel::navigateToHome,
        onNavBack = loginViewModel::navigateBack,
        setUseEnt = loginViewModel::setUseEnt,
        setEntName = loginViewModel::setEntName,
        setUsername = loginViewModel::setUsername,
        setPassword = loginViewModel::setPassword
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    setUseEnt: (Boolean) -> Unit,
    setEntName: (String) -> Unit,
    setUsername: (String) -> Unit,
    setPassword: (String) -> Unit,
    onNavToHome: () -> Unit,
    onNavBack: () -> Unit
) {
    val isEntMenuOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Add a Pronote Account")
        Row {
            Checkbox(
                checked = uiState.useEnt,
                onCheckedChange = {
                    setUseEnt(!uiState.useEnt)
                    isEntMenuOpen.value = !isEntMenuOpen.value
                }
            )
            Text("Use ENT")
        }
        if(uiState.useEnt) {
            Text("ENT Name: ${uiState.entName}")
            moi.choucroutemelba.thepronoteclient.ui.components.DropdownMenu(
                title = "Choose ENT...",
                items = buildMap { uiState.availableEnt.forEach { put(it.key) {
                    Text(
                        it.value,
                        color = MaterialTheme.colors.onSurface
                    ) }
                } },
                isExpanded = isEntMenuOpen.value,
                onExpand = { isEntMenuOpen.value = it },
                callback = {
                    setEntName(it as String)
                    Log.i("LoginScreen", "Selected ENT: ${uiState.availableEnt[it as String]}")
                },
            )
        }
        TextField(
            value = uiState.username,
            onValueChange = { setUsername(it) },
            label = { Text("Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        TextField(
            value = uiState.password,
            onValueChange = { setPassword(it) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password,
                autoCorrect = false,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = { onNavToHome() })
        )
        Button(onClick = { onNavBack() }) {
            Text("Login to ${uiState.username}")
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CustomWebView(
    url: String,
) {
    AndroidView(
        factory = { context: Context ->
            lateinit var webView: WebView
            WebView(context).apply {
                webView = this
                settings.javaScriptEnabled = true
                canGoBackOrForward(10)
                //settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/20100101 Firefox/105.0"

                webViewClient = PronoteWebViewClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
        modifier = Modifier.
                fillMaxSize(90/100F)
    )
}

class LoginUiStatePreviewProvider : PreviewParameterProvider<LoginUiState> {
    override val values: Sequence<LoginUiState>
        get() = sequenceOf(
            LoginUiState(
                url = "https://pronote.example.com"
            )
        )
}

@Composable
@Preview
fun LoginScreenPreview(@PreviewParameter(LoginUiStatePreviewProvider::class) uiState: LoginUiState) {
    LoginScreen(
        uiState = uiState,
        setUseEnt = {},
        setEntName = {},
        setUsername = {},
        setPassword = {},
        onNavToHome = {}
    ) {}
}