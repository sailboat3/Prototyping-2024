package pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.push
import defaultPrimaryVariant
import defaultSecondary
import getCurrentTheme
import nodes.RootNode
import org.jetbrains.compose.resources.ExperimentalResourceApi


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun MainMenu(
    modifier: Modifier,
    backStack: BackStack<RootNode.NavTarget>,
    robotStartPosition: MutableIntState,
    scoutName: MutableState<String>,
    comp: MutableState<String>,
    team: MutableIntState
) {

    Column(modifier = Modifier.verticalScroll(ScrollState(0))) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { backStack.push(RootNode.NavTarget.LoginPage) }, modifier = Modifier
                    .scale(0.75f)
                    .align(Alignment.CenterStart)
            ) {
                Text(text = "Log Out", color = getCurrentTheme().onPrimary)
            }

            Text(
                text = "Main Menu",
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
        Text(
            text = "Hello ${scoutName.value}",
            color = getCurrentTheme().onPrimary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        OutlinedButton(
            border = BorderStroke(3.dp, Color.Yellow),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            contentPadding = PaddingValues(horizontal = 60.dp, vertical = 5.dp),
            onClick = {
                backStack.push(RootNode.NavTarget.MatchMenu)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 50.dp, vertical = 50.dp),
        ) {
            Text(
                text = "Match",
                color = getCurrentTheme().onPrimary,
                fontSize = 35.sp
            )
        }
    }
}