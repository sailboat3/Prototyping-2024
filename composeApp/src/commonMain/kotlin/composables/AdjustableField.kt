package composables

import androidx.compose.runtime.Composable

@Composable
expect fun AdjustableField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
)