sed -i 's/import androidx.compose.ui.Modifier/import androidx.compose.ui.Modifier\nimport androidx.compose.foundation.layout.navigationBarsPadding/g' app/src/main/java/com/example/ui/components/BottomNavBar.kt
sed -i 's/modifier = Modifier/modifier = Modifier.navigationBarsPadding()/g' app/src/main/java/com/example/ui/components/BottomNavBar.kt
