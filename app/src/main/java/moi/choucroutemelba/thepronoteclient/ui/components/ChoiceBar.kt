package moi.choucroutemelba.thepronoteclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceBar(
    choices: Map<Any, String>,
    selectedChoice: Any,
    onChoiceSelected: (Any) -> Unit,
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier
) {
    LazyRow(horizontalArrangement = Arrangement.Center, modifier = modifier) {
        items(choices.size) { index ->
            ChoiceButton(
                choices.values.elementAt(index),
                selectedChoice == choices.keys.elementAt(index),
                onClick = { onChoiceSelected(choices.keys.elementAt(index)) },
                modifier = buttonModifier.fillMaxWidth(1f / choices.size)
            )
        }
    }
}

@Composable
fun ChoiceButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        ),
        border = if (isSelected) ButtonDefaults.outlinedBorder else null,
        elevation = ButtonDefaults.elevation(if(isSelected) 4.dp else 1.dp, 0.dp, 0.dp)
    ) {
        Text(text = text)
    }
}