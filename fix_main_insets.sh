sed -i 's/modifier = Modifier.fillMaxSize(),/modifier = Modifier.fillMaxSize().safeDrawingPadding(),/g' app/src/main/java/com/example/MainActivity.kt
sed -i '/import androidx.compose.foundation.layout.padding/a import androidx.compose.foundation.layout.safeDrawingPadding' app/src/main/java/com/example/MainActivity.kt
