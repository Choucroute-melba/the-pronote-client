package moi.choucroutemelba.thepronoteclient.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Show a fully featured dropdown menu with a button to display it and a dropdown
 * list of items to be chooses.
 *
 * @param title The title of the dropdown menu.
 * @param items The list of items to display in the dropdown menu and an ID that will be passed to the callback to
 * identify them.
 * @param isExpanded Whether the dropdown menu is isExpanded or not.
 * @param onExpand Called when the status of the dropdown menu is changed (isExpanded or not).
 * @param callback The function to call when an item is selected.
 */
@Composable
fun DropdownMenu(
    title: String,
    items: Map<Any, @Composable () -> Unit>,
    isExpanded: Boolean,
    onExpand: (Boolean) -> Unit = {},
    callback: (Any) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(90/100F)
        .height(50.dp)
) {
    var expanded = isExpanded
    Column (modifier = modifier){
        DropdownMenuButton(
            title,
            isExpanded,
            onClick = { expanded = !expanded; onExpand(expanded) },
            modifier = modifier.height(50.dp)
        )
        androidx.compose.material.DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { expanded = false; onExpand(expanded) },
            modifier = Modifier.wrapContentSize()
        ) {
            items.forEach { item ->
                androidx.compose.material.DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onExpand(expanded)
                        callback(item.key)
                    },
                ) {
                    item.value()
                }
            }
        }
    }
}

@Composable
@Preview
fun DropdownMenuPreview() {
    DropdownMenu(
        "Dropdown menu",
        mapOf(
            "url1" to { Text("Item 1", color = MaterialTheme.colors.onSurface) },
            "url2" to { Text("Item 2", color = MaterialTheme.colors.onSurface) },
            "url3" to { Text("Item 3", color = MaterialTheme.colors.onSurface) },
            "url4" to { Text("Item 4", color = MaterialTheme.colors.onSurface) },
        ),
        true,
        callback = {  }
    )
}

/**
 * A button to use to introduce Compose DropdownMenu.
 *
 * @param modifier Modifier to be applied to the button (used to define the width, height and position for the button).
 * @param text The text to display on the button.
 * @param expanded Whether the dropdown menu is expanded or not.
 * @param onClick The function to call when the button is clicked.
 */
@Composable
fun DropdownMenuButton(
    text: String,
    expanded: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(90/100F)
        .height(50.dp)
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Text(text)
        if(expanded) Text(" ▲") else Text(" ▼")
    }
}

@Composable
@Preview
fun DropDownMenuButtonPreview(
) {
    var expanded = true
    DropdownMenuButton("Dropdown menu",
        modifier = Modifier.width(300.dp)
            .height(50.dp),
        onClick = { expanded = !expanded },
        expanded = expanded
    )
}