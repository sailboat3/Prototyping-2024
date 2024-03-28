package nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import pages.AutoTeleSelectorMenu

class AutoTeleSelectorNode(
    buildContext: BuildContext,
    private var robotStartPosition: MutableIntState,
    private val team: MutableIntState,
    private val mainMenuBackStack: BackStack<RootNode.NavTarget>,
    private val backStack: BackStack<NavTarget> = BackStack(
        model = BackStackModel(
            initialTarget = NavTarget.AutoScouting,
            savedStateMap = buildContext.savedStateMap
        ),
        visualisation = { BackStackFader(it) }
    )
) : ParentNode<AutoTeleSelectorNode.NavTarget>(
    appyxComponent = backStack,
    buildContext = buildContext
) {
    private val selectAuto = mutableStateOf(false)


    sealed class NavTarget : Parcelable {
        @Parcelize
        data object AutoScouting : NavTarget()

        @Parcelize
        data object TeleScouting : NavTarget()
    }

    override fun resolve(interactionTarget: NavTarget, buildContext: BuildContext): Node =
        when (interactionTarget) {
            NavTarget.AutoScouting -> AutoNode(buildContext, backStack, mainMenuBackStack, selectAuto, match, team, robotStartPosition)
            NavTarget.TeleScouting -> TeleNode(buildContext, backStack, selectAuto, match, team, robotStartPosition)
        }

    @Composable
    override fun View(modifier: Modifier) {
        Column {
            AutoTeleSelectorMenu(match, team, robotStartPosition, selectAuto, backStack)
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}


val f1 = mutableIntStateOf(0)
val f2 = mutableIntStateOf(0)
val f3 = mutableIntStateOf(0)
val m1 = mutableIntStateOf(0)
val m2 = mutableIntStateOf(0)
val m3 = mutableIntStateOf(0)
val m4 = mutableIntStateOf(0)
val m5 = mutableIntStateOf(0)

val match = mutableStateOf("1")
val autoSpeakerNum = mutableIntStateOf(0)
val autoAmpNum = mutableIntStateOf(0)
val collected = mutableIntStateOf(0)
val autoSMissed = mutableIntStateOf(0)
val autoAMissed = mutableIntStateOf(0)
val teleSpeakerNum  =  mutableIntStateOf(0)
val teleAmpNum  = mutableIntStateOf(0)
val teleTrapNum = mutableIntStateOf(0)
val teleSMissed = mutableIntStateOf(0)
val teleAMissed = mutableIntStateOf(0)
var autoNotes = mutableStateOf("")
var teleNotes = mutableStateOf("")

fun createOutput(team: MutableIntState, robotStartPosition: MutableIntState): String {
    val teleNotesFinal = if (teleNotes.value == "") "No Comments" else teleNotes.value
    val autoNotesFinal = if (autoNotes.value == "") "No Comments" else autoNotes.value
    return delimString("/",
        match.value,
        team.value.toString(),
        robotStartPosition.value.toString(),
        autoSpeakerNum.value.toString(),
        autoAmpNum.value.toString(),
        autoSMissed.value.toString(),
        autoAMissed.value.toString(),
        f1.value.toString(),
        f2.value.toString(),
        f3.value.toString(),
        m1.value.toString(),
        m2.value.toString(),
        m3.value.toString(),
        m4.value.toString(),
        m5.value.toString(),
        teleSpeakerNum.value.toString(),
        teleAmpNum.value.toString(),
        teleTrapNum.value.toString(),
        teleSMissed.value.toString(),
        teleAMissed.value.toString(),
        autoNotesFinal,
        teleNotesFinal
    )
}

private fun delimString(delimiter: String, vararg inputs: String) : String {
    val endString = StringBuilder()
    inputs.forEach { endString.append (it + delimiter) }
    endString.deleteAt(endString.lastIndex)
    return endString.toString()
}