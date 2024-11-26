package composables


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import defaultPrimaryVariant
import getCurrentTheme


/**
 * A composable function that renders an adjustable numeric field with a label, a minus button,
 * a plus button, and an outlined text field for direct numeric input. The value is constrained
 * to be non-negative.
 *
 * @param label The label text for the field.
 * @param value The current integer value of the field.
 * @param onValueChange A callback function that is invoked with the new value when it changes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun AdjustableField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
            onClick = { onValueChange((value - 1).coerceAtLeast(0)) }
        ) {
            Text("-")
        }
        OutlinedTextField(
            value = value.toString(),
            onValueChange = { newValue ->
                onValueChange((newValue.toIntOrNull() ?: 0).coerceAtLeast(0))
            },
            shape = RoundedCornerShape(8.dp),
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = getCurrentTheme().background,
                unfocusedTextColor = getCurrentTheme().onPrimary,
                focusedContainerColor = getCurrentTheme().background,
                focusedTextColor = getCurrentTheme().onPrimary,
                focusedLabelColor = Color.Cyan,
                cursorColor = getCurrentTheme().onSecondary,
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = getCurrentTheme().secondary,
            ),
            modifier = Modifier
                .padding(8.dp)
        )
        Button(
            border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
            onClick = { onValueChange(value + 1) }
        ) {
            Text("+")
        }
    }
}