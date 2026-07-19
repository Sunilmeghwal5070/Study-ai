sed -i 's/Column {/Column(modifier = Modifier.weight(1f)) {/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/Row(verticalAlignment = Alignment.CenterVertically) {/Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/color = Color.White)/color = Color.White, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/color = Color.White.copy(alpha = 0.8f))/color = Color.White.copy(alpha = 0.8f), maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
