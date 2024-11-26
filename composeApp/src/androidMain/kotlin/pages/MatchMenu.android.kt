package pages

import DiscordWebhook
import Scorecard
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
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

/**
 * A mutable list of `Scorecard` objects that holds the scorecards for a game or a series of games.
 *
 * This list is used to keep track of all the scorecards dynamically,
 * allowing for additions, deletions, and updates to the scores.
 *
 * The data type used is `mutableStateListOf`,
 * which provides a state-aware observable list that can be used within a Compose UI.
 *
 * Each `Scorecard` object in the list represents the scores for a particular game or player,
 * containing all necessary information related to scoring.
 */
val scorecardList = mutableStateListOf<Scorecard>()

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


    val webhook = DiscordWebhook("YOUR_WEBHOOK_URL")


    // Mutable Values
    var highGoalValue by remember { mutableStateOf(0) }
    var midGoalValue by remember { mutableStateOf(0) }
    var lowGoalValue by remember { mutableStateOf(0) }

    var mountingBonus by remember { mutableStateOf(false) }
    var sizeBonus by remember { mutableStateOf(false) }
    var weightBonus by remember { mutableStateOf(false) }
    var motorBonus by remember { mutableStateOf(false) }

    // Point Values
    val pointsFromAssets = (highGoalValue * 5) + (midGoalValue * 3) + (lowGoalValue * 2)
    val interest = (
            floor((highGoalValue + midGoalValue + lowGoalValue).toDouble() / 3) * 20 + floor((highGoalValue + midGoalValue + lowGoalValue).toDouble() / 12) * 100
            )
    val totalPointsFromAssets = pointsFromAssets + interest
    val totalPointsFromBonuses =
        (mountingBonus.compareTo(false) + sizeBonus.compareTo(false) + weightBonus.compareTo(false) + motorBonus.compareTo(
            false
        )) * 5
    var penaltyPoints by remember { mutableStateOf(0) }
    val totalPoints = totalPointsFromAssets + totalPointsFromBonuses - penaltyPoints

    val scrollState = rememberScrollState() // Avoid resetting the scroll upon recomposition

    val context = LocalContext.current

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
        val options = listOf("BlackRock", "Vanguard", "Fidelity", "JPMorgan", "State Street")
        var selectedOption by remember { mutableStateOf("") }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { selectedOption = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Team Name") },
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
                singleLine = true,
                trailingIcon = {
                    Icon(
                        icon, "contentDescription",
                        Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { expanded = !expanded }
                    )

                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .border(BorderStroke(1.dp, defaultPrimaryVariant)) // Adding white border
            ) {
                options.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = label
                            expanded = false
                        },
                        text = {
                            Text(
                                text = label,
                                color = getCurrentTheme().onPrimary
                            )
                        }
                    )
                }
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
        Column {
            BonusCheckbox(
                "Mounted and Unmounted in under 3 minutes",
                mountingBonus,
                onValueChange = { newValue -> mountingBonus = newValue })
            BonusCheckbox(
                "Fits in a 16\"x16\"x14\" Box",
                sizeBonus,
                onValueChange = { newValue -> sizeBonus = newValue })
            BonusCheckbox(
                "Weighs Less Than 7.5 Pounds",
                weightBonus,
                onValueChange = { newValue -> weightBonus = newValue })
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
        Row {
            Button(
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary),
                modifier = Modifier.padding(4.dp),
                onClick = { penaltyPoints = (penaltyPoints - 5).takeIf { it >= 0 } ?: penaltyPoints }
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
                onClick = { penaltyPoints += 30 }
            ) {
                Text("+30")
            }
        }

        Text(
            text = "Quick Add Penalties",
            color = getCurrentTheme().onPrimary
        )
        Row {
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
        Row {
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
            onClick = {
                if (selectedOption != "") {
                    val newScorecard = Scorecard(
                        teamName = selectedOption,
                        matchNumber = 1,
                        totalScore = totalPoints,
                        penaltyScore = penaltyPoints,
                        bonusScore = totalPointsFromBonuses,
                        highGoalAmount = highGoalValue,
                        midGoalAmount = midGoalValue,
                        lowGoalAmount = lowGoalValue
                    )
                    scorecardList.add(newScorecard)

                    webhook.sendToWebhook(newScorecard)

                    backStack.push(RootNode.NavTarget.MainMenu)
                } else {
                    Toast.makeText(
                        context,
                        "Please select a team",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Text(
                text = "Submit Score",
                fontSize = 24.sp
            )
        }
    }
}
