package moi.choucroutemelba.thepronoteclient.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.gson.Gson
import moi.choucroutemelba.thepronoteclient.data.features.ApiErrorType
import moi.choucroutemelba.thepronoteclient.data.features.HttpException
import moi.choucroutemelba.thepronoteclient.data.pronote.PronoteWebViewClient
import moi.choucroutemelba.thepronoteclient.ui.components.ChoiceBar
import moi.choucroutemelba.thepronoteclient.ui.components.HttpErrorRender

@Composable
fun LoginRoute(
    loginViewModel: LoginViewModel,
) {

    LoginScreen(
        uiState = loginViewModel.uiState,
        setUseEnt = loginViewModel::setUseEnt,
        setEntName = loginViewModel::setEntName,
        setUsername = loginViewModel::setUsername,
        setPassword = loginViewModel::setPassword,
        setPostalCode = loginViewModel::setPostalCode,
        setSelectionMethod = loginViewModel::setSelectionMethod,
        getEstablishmentList = loginViewModel::getEstablishmentList,
        onNavToHome = loginViewModel::navigateToHome,
        onNavBack = loginViewModel::navigateBack,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    setUseEnt: (Boolean) -> Unit,
    setEntName: (String) -> Unit,
    setUsername: (String) -> Unit,
    setPassword: (String) -> Unit,
    setPostalCode: (String) -> Unit,
    setSelectionMethod: (SelectionMethod) -> Unit,
    getEstablishmentList: () -> Unit,
    onNavToHome: () -> Unit,
    onNavBack: () -> Unit
) {
    val isEntMenuOpen = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().scrollable(rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        Text("Add a Pronote Account")
        ChoiceBar(
            choices = mapOf(
                SelectionMethod.GEOLOCATION to "Geolocation",
                SelectionMethod.URL to "URL",
            ),
            selectedChoice = uiState.selectionMethod,
            onChoiceSelected = {
                setSelectionMethod(it as SelectionMethod)
            },
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        )
        if(uiState.error != null) {
            var text = "Error (${uiState.error!!.type})"
            if(uiState.error!!.type == ApiErrorType.HTTP_ERROR && uiState.error!!.e != null) {
                val e: HttpException = uiState.error!!.e as HttpException
                HttpErrorRender(e, {})
            }
            text += "\n${uiState.error!!.message}"
            Text(text, color = MaterialTheme.colors.error)
        }
        if(uiState.selectionMethod == SelectionMethod.GEOLOCATION) {
            GeolocationSelection(
                uiState = uiState,
                setPostalCode = setPostalCode,
                onNavToHome = onNavToHome,
                getEstablishments = getEstablishmentList
            )
            Text(Gson().toJson(uiState.establishments))
        }
        else if(uiState.selectionMethod == SelectionMethod.URL) {
            UrlSelection(
                uiState = uiState,
                setUrl = setPostalCode,
                onNavToHome = onNavToHome,
            )
            Text(Gson().toJson(uiState.availableEnt))
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

@Composable
fun GeolocationSelection(
    uiState: LoginUiState,
    setPostalCode: (String) -> Unit,
    getEstablishments: () -> Unit,
    onNavToHome: () -> Unit,
) {
    Column {
        Text("Find your establishment")
        if(uiState.loadingEstablishments)
            LinearProgressIndicator()
        TextField(
            value = uiState.postalCode ?: "",
            onValueChange = { setPostalCode(it) },
            label = { Text("Postal Code") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Number,
                autoCorrect = false,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = { getEstablishments() })
        )
        Button(onClick = { getEstablishments() }) {
            Text("Search for ${uiState.postalCode}")
        }
    }
}

@Composable
fun UrlSelection(
    uiState: LoginUiState,
    setUrl: (String) -> Unit,
    onNavToHome: () -> Unit,
) {
    Column {
        Text("Add a Pronote Account using the URL provided by your school")
        if (uiState.loadingEntList)
            LinearProgressIndicator()
        TextField(
            value = uiState.url ?: "",
            onValueChange = { setUrl(it) },
            label = { Text("URL") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        Button(onClick = { onNavToHome() }) {
            Text("Connect to ${uiState.url?.subSequence(8, 27)}")
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
        setPostalCode = {},
        setSelectionMethod = {},
        getEstablishmentList = {},
        onNavToHome = {},
    ) {}
}