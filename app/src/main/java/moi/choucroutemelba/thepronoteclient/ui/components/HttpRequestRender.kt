package moi.choucroutemelba.thepronoteclient.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moi.choucroutemelba.thepronoteclient.data.features.HttpException

@Composable
fun HttpErrorRender(
    error: HttpException,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = Modifier.scrollable(rememberScrollState(), orientation = Orientation.Horizontal)) { Column {
        Text(text = "HTTP Error : ${error.code} - ${error.codeMessage}", color = MaterialTheme.colors.error)
        Text(text = "- ${error.message ?: "No more information"}", color = MaterialTheme.colors.error)
        Text(text = "- ${error.serverMessage ?: "No server message"}", color = MaterialTheme.colors.error)
    } }
}