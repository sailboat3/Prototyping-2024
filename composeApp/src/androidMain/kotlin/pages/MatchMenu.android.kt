package pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import composables.AdjustableField
import composables.BonusCheckbox
import defaultPrimaryVariant
import getCurrentTheme
import nodes.RootNode
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.math.floor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun MatchMenu(
    modifier: Modifier,
    backStack: BackStack<RootNode.NavTarget>,
    robotStartPosition: MutableIntState,
    scoutName: MutableState<String>,
    comp: MutableState<String>,
    team: MutableIntState
) {
    // Mutable Values
    var highGoalValue by remember { mutableStateOf(0) }
    var midGoalValue by remember { mutableStateOf(0) }
    var lowGoalValue by remember { mutableStateOf(0) }

    var mountingBonus by remember { mutableStateOf(false) }
    var sizeBonus by remember { mutableStateOf(false) }
    var weightBonus by remember { mutableStateOf(false) }
    var motorBonus by remember { mutableStateOf(false) }

    // Point Values
    var pointsFromAssets = (highGoalValue * 5) + (midGoalValue * 3) + (lowGoalValue * 2)
    var interest = (
            floor((highGoalValue + midGoalValue + lowGoalValue).toDouble() / 3) * 20 +
            floor((highGoalValue + midGoalValue + lowGoalValue).toDouble() / 12) * 100
            )
    var totalPointsFromAssets = pointsFromAssets + interest
    var totalPointsFromBonuses = (mountingBonus.compareTo(false) + sizeBonus.compareTo(false) + weightBonus.compareTo(false) + motorBonus.compareTo(false)) * 5
    var penaltyPoints by remember { mutableStateOf(0) }
    var totalPoints = totalPointsFromAssets + totalPointsFromBonuses - penaltyPoints

    var scrollState = rememberScrollState() // Avoid resetting the scroll upon recomposition
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { backStack.push(RootNode.NavTarget.MainMenu) }, modifier = Modifier
                    .scale(0.75f)
                    .align(Alignment.CenterStart)
            ) {
                Text(text = "Cancel Match", color = getCurrentTheme().onPrimary)
            }

            Text(
                text = "Match",
                fontSize = 32.sp,
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            )

        }
        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )


        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf("Select a Team") }
        val options = listOf("a", "b", "c")
        var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown


        OutlinedTextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            readOnly = true,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = {Text("Team")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = getCurrentTheme().background,
                unfocusedTextColor = getCurrentTheme().onPrimary,
                focusedContainerColor = getCurrentTheme().background,
                focusedTextColor = getCurrentTheme().onPrimary,
                focusedLabelColor = getCurrentTheme().onSecondary,
                cursorColor = getCurrentTheme().onSecondary,
                focusedBorderColor = getCurrentTheme().onSecondary,
                unfocusedBorderColor = getCurrentTheme().secondary,
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            options.forEachIndexed { index, item ->
                DropdownMenuItem(text = {
                    Text(text = item)
                },
                    onClick = {
                        expanded = false
                        selectedOption = item
                    },
                    colors = MenuItemColors(
                        textColor = getCurrentTheme().onPrimary,
                        leadingIconColor = getCurrentTheme().onPrimary,
                        trailingIconColor = getCurrentTheme().onPrimary,
                        disabledTextColor = getCurrentTheme().secondary,
                        disabledLeadingIconColor = getCurrentTheme().secondary,
                        disabledTrailingIconColor = getCurrentTheme().secondary,
                    )
                )
            }
        }


        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )
        // Stock Market
        Text(
            text = "Stock Market",
            color = getCurrentTheme().onPrimary
        )
        AdjustableField("High Goal", highGoalValue, onValueChange = { newValue -> highGoalValue = newValue })
        AdjustableField("Mid Goal", midGoalValue, onValueChange = { newValue -> midGoalValue = newValue })
        AdjustableField("Low Goal", lowGoalValue, onValueChange = { newValue -> lowGoalValue = newValue })

        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )

        // Bonus Points
        Text(
            text = "Bonus Points",
            color = getCurrentTheme().onPrimary
        )
        Column() {
            BonusCheckbox("Mounted and Unmounted in under 3 minutes", mountingBonus, onValueChange = { newValue -> mountingBonus = newValue })
            BonusCheckbox("Fits in a 16\"x16\"x14\" Box", sizeBonus, onValueChange = { newValue -> sizeBonus = newValue })
            BonusCheckbox("Weighs Less Than 7.5 Pounds", weightBonus, onValueChange = { newValue -> weightBonus = newValue })
            BonusCheckbox("Uses 0-1 Motors", motorBonus, onValueChange = { newValue -> motorBonus = newValue })
        }

        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )

        // Penalties
        Text(
            text = "Penalties",
            color = getCurrentTheme().onPrimary
        )

        AdjustableField("Penalty Amount", penaltyPoints, onValueChange = { newValue -> penaltyPoints = newValue })
        Row() {
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints = (penaltyPoints- 5).takeIf { it >= 0 } ?: penaltyPoints }
            ) {
                Text("-5")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 5 }
            ) {
                Text("+5")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 10 }
            ) {
                Text("+10")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 20 }
            ) {
                Text("+20")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 20 }
            ) {
                Text("+30")
            }
        }

        Text(
            text = "Quick Add Penalties",
            color = getCurrentTheme().onPrimary
        )
        Row() {
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 20 }
            ) {
                Text("Burned a Motor")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 5 }
            ) {
                Text("Elevator Malfunction")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 30 }
            ) {
                Text("Human Interference")
            }
        }
        Row() {
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 15 }
            ) {
                Text("More than 2 Balls in Possession")
            }
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints += 30 }
            ) {
                Text("Robot Outside Field")
            }
        }

        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )

        // Point Tallies
        Text(
            text = "Points From Assets: $pointsFromAssets",
            color = getCurrentTheme().onPrimary
        )
        Text(
            text = "Interest: $interest",
            color = getCurrentTheme().onPrimary
        )
        Text(
            text = "Bonus Points: $totalPointsFromBonuses",
            color = getCurrentTheme().onPrimary
        )
        Text(
            text = "Penalty: " + -penaltyPoints,
            color = getCurrentTheme().onPrimary
        )
        Text(
            text = "Total Points: $totalPoints",
            color = getCurrentTheme().onPrimary
        )
        Button(
            border = BorderStroke(color = Color.Cyan, width = 2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
            modifier = Modifier.padding(4.dp),
            onClick = { backStack.push(RootNode.NavTarget.MainMenu) }
        ) {
            Text(
                text = "Submit Score",
                fontSize = 24.sp
            )
        }
    }
}