cat << 'INNER_EOF' >> app/src/main/java/com/example/ui/screens/HistoryScreen.kt

    if (selectedItem.value != null) {
        AlertDialog(
            onDismissRequest = { selectedItem.value = null },
            title = { Text("Q: ${selectedItem.value!!.question}", maxLines = 3, overflow = TextOverflow.Ellipsis) },
            text = { 
                androidx.compose.foundation.lazy.LazyColumn {
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
    }
INNER_EOF
