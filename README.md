A simple suspending API to show popups such as dialogs and bottom sheets.

### Install

libs.versions.toml

```toml
[versions]
popuphost = "1.0.0"

[dependencies]
popuphost = { group = "io.github.andannn", name = "popuphost", version.ref = "popuphost" }
```

then

```
dependencies {
    implementation(libs.popuphost)
}
```

### Quick Start

1. Define DialogId 
```kotlin
object DialogA : PopupId<Long>
```

2. Setup PopupHost
```kotlin
@Composable
fun App() {
    val popupHostState = remember { PopupHostState() }
    PopupHost(
        popupHostState = popupHostState,
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
        }
    )
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

```

3. Show popup
```kotlin
val result: Long? = popupHostState.showDialog(DialogA)
```
