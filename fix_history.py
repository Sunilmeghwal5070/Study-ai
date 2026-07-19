import re

with open("app/src/main/java/com/example/ui/screens/HistoryScreen.kt", "r") as f:
    content = f.read()

# Add icons
content = content.replace("import androidx.compose.material.icons.filled.Delete", "import androidx.compose.material.icons.filled.Delete\nimport androidx.compose.material.icons.filled.ContentCopy\nimport androidx.compose.material.icons.filled.Share")
content = content.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport android.content.Context\nimport android.content.ClipboardManager\nimport android.content.ClipData\nimport android.content.Intent")

dialog_old = """    if (selectedItem.value != null) {
        AlertDialog(
            onDismissRequest = { selectedItem.value = null },
            title = { Text("Q: ${selectedItem.value!!.question}", maxLines = 3, overflow = TextOverflow.Ellipsis) },
            text = { 
                LazyColumn {
                    item {
                        Text(selectedItem.value!!.answer) 
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedItem.value = null }) {
                    Text("Close")
                }
            }
        )
    }"""

dialog_new = """    if (selectedItem.value != null) {
        val context = androidx.compose.ui.platform.LocalContext.current
        AlertDialog(
            onDismissRequest = { selectedItem.value = null },
            title = { Text("Q: ${selectedItem.value!!.question}", maxLines = 3, overflow = TextOverflow.Ellipsis) },
            text = { 
                LazyColumn {
                    item {
                        Text(selectedItem.value!!.answer) 
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedItem.value = null }) {
                    Text(com.example.ui.utils.AppStrings.get("close", lang))
                }
            },
            dismissButton = {
                Row {
                    IconButton(onClick = {
                        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Answer", "Q: ${selectedItem.value!!.question}\\n\\nA: ${selectedItem.value!!.answer}")
                        clipboardManager.setPrimaryClip(clip)
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    }
                    IconButton(onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Q: ${selectedItem.value!!.question}\\n\\nA: ${selectedItem.value!!.answer}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            }
        )
    }"""

content = content.replace(dialog_old, dialog_new)

with open("app/src/main/java/com/example/ui/screens/HistoryScreen.kt", "w") as f:
    f.write(content)
