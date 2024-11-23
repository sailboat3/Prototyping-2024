package composables

import androidx.compose.runtime.Composable

@Composable
expect fun BonusCheckbox(
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
)