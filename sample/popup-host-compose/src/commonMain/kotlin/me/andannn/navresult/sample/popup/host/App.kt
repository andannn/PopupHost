package me.andannn.navresult.sample.popup.host

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andannn.popup.DialogFactoryProvider
import io.github.andannn.popup.PopupEntry
import io.github.andannn.popup.PopupFactory
import io.github.andannn.popup.PopupFactoryProvider
import io.github.andannn.popup.PopupFactoryScope
import io.github.andannn.popup.PopupHost
import io.github.andannn.popup.PopupHostState
import io.github.andannn.popup.PopupId
import io.github.andannn.popup.entryProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleApp() {
    val popupHostState = remember { PopupHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sample") },
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            var popupResultA: Long? by remember {
                mutableStateOf(null)
            }
            Button(
                onClick = {
                    scope.launch {
                        val result = popupHostState.showDialog(DialogA)
                        if (result != null) {
                            popupResultA = result
                        }
                    }
                },
            ) {
                Text(
                    text = "Show dialog A",
                )
            }


            if (popupResultA != null) {
                Text(
                    text = "popupResultA: $popupResultA",
                )
            }

            var popupResultB: DialogWitParam.Result? by remember {
                mutableStateOf(null)
            }
            Button(
                onClick = {
                    scope.launch {
                        val result = popupHostState.showDialog(DialogWitParam("INPUT PARAM"))
                        if (result != null) {
                            popupResultB = result
                        }
                    }
                },
            ) {
                Text(
                    text = "Show dialog with param",
                )
            }

            if (popupResultB != null) {
                Text(
                    text = "popupResultB: $popupResultB",
                )
            }

            var bottomSheetResultA: Long? by remember {
                mutableStateOf(null)
            }
            Button(
                onClick = {
                    scope.launch {
                        val result = popupHostState.showDialog(CustomBottomSheetA)
                        if (result != null) {
                            bottomSheetResultA = result
                        }
                    }
                },
            ) {
                Text(
                    text = "Show custom bottom sheet",
                )
            }

            if (bottomSheetResultA != null) {
                Text(
                    text = "bottomSheetResultA: $bottomSheetResultA",
                )
            }
        }
    }

    PopupHost(
        popupHostState = popupHostState,
        popupFactoryProvider = listOf(
            DialogFactoryProvider(),
            ModalBottomSheetFactoryProvider(),
        ),
        entryProvider = entryProvider {
            entry(
                metadata = DialogFactoryProvider.metadata()
            ) { dialogId, onAction ->
                Surface(
                    modifier = Modifier.wrapContentSize(),
                    shape = AlertDialogDefaults.shape,
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                ) {
                    DialogAContent(dialogId, onAction)
                }
            }

            entry(
                metadata = DialogFactoryProvider.metadata()
            ) { dialogId, onAction ->
                Surface(
                    modifier = Modifier.wrapContentSize(),
                    shape = AlertDialogDefaults.shape,
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                ) {
                    DialogWithParam(dialogId, onAction)
                }
            }

            entry(
                metadata = ModalBottomSheetFactoryProvider.bottomSheet()
            ) { dialogId, onAction ->
                CustomBottomSheetAContent(dialogId, onAction)
            }
        }
    )
}

@Composable
fun CustomBottomSheetAContent(dialogId: CustomBottomSheetA, onAction: (Long) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bottom Sheet A")
        Button(
            onClick = {
                onAction(456L)
            },
        ) {
            Text(
                text = "Set Result",
            )
        }
    }
}

@Composable
fun DialogWithParam(dialogId: DialogWitParam, onAction: (DialogWitParam.Result) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Dialog DialogWithParam")
        Text(text = "input : ${dialogId.param}")
        Button(
            onClick = {
                onAction(DialogWitParam.Result.Success)
            },
        ) {
            Text(
                text = "Set Result success",
            )
        }
        Button(
            onClick = {
                onAction(DialogWitParam.Result.Failure)
            },
        ) {
            Text(
                text = "Set Result Failure",
            )
        }
    }

}

@Composable
fun DialogAContent(dialogA: DialogA, onAction: (Long) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Dialog A")
        Button(
            onClick = {
                // set Result
                onAction(123L)
            },
        ) {
            Text(
                text = "Set Result",
            )
        }
    }
}

object DialogA : PopupId<Long>
object CustomBottomSheetA : PopupId<Long>

class DialogWitParam(
    val param: String
) : PopupId<DialogWitParam.Result> {
    sealed interface Result {
        data object Success : Result
        data object Failure : Result
    }
}



class ModalBottomSheetFactoryProvider : PopupFactoryProvider {
    override fun create(entry: PopupEntry<*>): PopupFactory? =
        entry.metadata[BOTTOM_SHEET_KEY]?.let {
            ModalBottomSheetFactory(
                entry = entry,
            )
        }

    companion object {
        internal const val BOTTOM_SHEET_KEY = "bottom_sheet"

        fun bottomSheet(): Map<String, Any> = mapOf(BOTTOM_SHEET_KEY to Unit)
    }
}

data class ModalBottomSheetFactory(
    private val entry: PopupEntry<*>,
) : PopupFactory {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun PopupFactoryScope.Content() {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = onRequestDismiss,
            content = {
                entry.Content(
                    onAction = {
                        onPerformAction(it)
                    },
                )
            },
        )
    }
}
