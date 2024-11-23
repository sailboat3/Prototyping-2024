package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import defaultError
import defaultPrimaryVariant
import deleteFile
import getCurrentTheme
import nodes.RootNode
import nodes.matchScoutArray
import nodes.reset
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun LoginMenu(
    backStack: BackStack<RootNode.NavTarget>,
    scoutName: MutableState<String>,
    comp: MutableState<String>
) {
    val logo = File("Logo.png")
    var deleteData by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 2.dp,
            modifier = Modifier
                .padding(8.dp)
        )
        OutlinedTextField(
            value = scoutName.value,
            onValueChange = { scoutName.value = it },
            placeholder = { Text("First & Last Name") },
            shape = RoundedCornerShape(8.dp),
            label = { Text(text = "Name") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = getCurrentTheme().background,
                unfocusedTextColor = getCurrentTheme().onPrimary,
                focusedContainerColor = getCurrentTheme().background,
                focusedTextColor = getCurrentTheme().onPrimary,
                focusedLabelColor = Color.Cyan,
                cursorColor = getCurrentTheme().onSecondary,
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = getCurrentTheme().secondary
            ),
            modifier = Modifier
                .padding(8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedButton(
                onClick = { deleteData = true },
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary)
            ) {
                Text(
                    text = "Delete Data",
                    color = getCurrentTheme().onPrimary
                )
            }
            if (deleteData) {
                BasicAlertDialog(
                    onDismissRequest = { deleteData = false },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(5.dp)
                        )
                        .border(BorderStroke(3.dp, defaultPrimaryVariant), RoundedCornerShape(5.dp))

                ) {
                    Column {
                        Text(text = "Are you sure?")
                        Box(modifier = Modifier.fillMaxWidth(8f / 10f)) {
                            Button(
                                onClick = {
                                    deleteData = false; matchScoutArray.clear(); reset(); deleteFile(context)
                                },
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Text(text = "Yes", color = defaultError)
                            }

                            Button(
                                onClick = { deleteData = false },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Text(text = "No", color = defaultError)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.padding(8.dp))
            OutlinedButton(
                onClick = {
                    if (scoutName.value != "")
                        backStack.push(RootNode.NavTarget.MainMenu)
                },
                border = BorderStroke(color = defaultPrimaryVariant, width = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = getCurrentTheme().primary)
            ) {
                Text(
                    text = "Login",
                    color = getCurrentTheme().onPrimary
                )
            }
        }

    }


}
