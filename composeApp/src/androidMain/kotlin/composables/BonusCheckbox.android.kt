package composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun BonusCheckbox(
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = value,
            onCheckedChange = { newValue ->
                onValueChange(!value)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Cyan,
                uncheckedColor = Color.Yellow
            )
        )
        Text(text = label)
    }
}