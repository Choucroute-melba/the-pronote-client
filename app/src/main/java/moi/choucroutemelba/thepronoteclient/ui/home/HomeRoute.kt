package moi.choucroutemelba.thepronoteclient.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import moi.choucroutemelba.thepronoteclient.data.pronote.api.PronoteData
import moi.choucroutemelba.thepronoteclient.data.pronote.user.Result

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel
) {
    HomeScreen(
        uiState = homeViewModel.uiState,
        onIncrement = { homeViewModel.increment() },
        onUrlChange = { homeViewModel.onUrlChange(it) },
        onGetTestSite = { homeViewModel.getTestSite() },
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onIncrement: () -> Unit,
    onUrlChange: (String) -> Unit,
    onGetTestSite: () -> Unit
) {
    Column {
        if(uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column {
            Button(onClick = {onIncrement()}) {
                Text(text = "count: ${uiState.count}")
            }
            Row (modifier = Modifier.fillMaxWidth()) {
                TextField(value = uiState.urlValue,
                    onValueChange = {onUrlChange(it)},
                    placeholder = {Text(text = "id")},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri, imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {onGetTestSite()}),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(75/100F)
                )
                Button(onClick = {onGetTestSite()}) {
                    Text(text = (if(uiState.isLoading) "loading" else "test"))
                }
            }
        }
        if(uiState.isLoading)
            Text(text = "loading")
        else if(uiState.isError)
            Text(text = "error: ${uiState.testError}", color = Color.Red)
        else if(uiState.isSuccess) {
            LazyColumn {
                item {
                    LazyRow {
                        item {
                            Column {
                                Text(text = "response from ${uiState.location}")
                                Text(text = if (uiState.responseText.length > 20000) uiState.responseText.substring(0, 20000) else uiState.responseText,
                                    style = TextStyle(fontFamily = FontFamily.Monospace)
                                )
                            }
                        }
                    }
                }
            }
        }
        else Text(text = "nothing")
    }
}

class HomeUiStatePreviewProvider: PreviewParameterProvider<HomeUiState> {
    override val values = sequenceOf(
        HomeUiState(),
        HomeUiState(count = 1, response = Result.Success(PronoteData("{data: data}", "https://pronote.example.com"))),
    )
}

@Preview
@Composable
fun HomeScreenPreview(@PreviewParameter(HomeUiStatePreviewProvider::class) uiState: HomeUiState) {
    HomeScreen(
        uiState = uiState,
        onIncrement = {},
        onUrlChange = {},
        onGetTestSite = {}
    )
}