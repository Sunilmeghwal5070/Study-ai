import re

with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "r") as f:
    content = f.read()

# Replace get settings
if "val settings by viewModel.userSettings.collectAsState()" not in content:
    content = content.replace("val lang = \"en\"", "val settings by viewModel.userSettings.collectAsState()\n    val lang = settings?.language ?: \"en\"")
else:
    content = content.replace("val lang = settings?.language ?: \"en\"", "val lang = settings?.language ?: \"en\"\n    val context = androidx.compose.ui.platform.LocalContext.current")

# Add to translate callback
old_callback = """viewModel.translateText(text, fromLang, toLang) {
                                        translatedText = it
                                    }"""
new_callback = """viewModel.translateText(text, fromLang, toLang) {
                                        translatedText = it
                                        com.example.ui.utils.MediaUtils.vibrate(context, settings?.vibrationEnabled == true)
                                        com.example.ui.utils.MediaUtils.speak(it, if(toLang == "Hindi") "hi" else "en", settings?.voiceEnabled == true)
                                    }"""
content = content.replace(old_callback, new_callback)

# Replace Handle TTS
content = content.replace("IconButton(onClick = { /* Handle TTS */ }) {", "IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(translatedText, if(toLang == \"Hindi\") \"hi\" else \"en\", true) }) {")

# Also add animation to swap button and copy button to translated text
# Find the swap button
content = content.replace(
    "IconButton(onClick = {",
    "var isSwapped by remember { mutableStateOf(false) }\n"
    "                    val rotation by androidx.compose.animation.core.animateFloatAsState(targetValue = if (isSwapped) 180f else 0f)\n"
    "                    IconButton(onClick = {"
)
content = content.replace(
    "Icon(Icons.Default.SwapHoriz, contentDescription = \"Swap\")",
    "Icon(Icons.Default.SwapHoriz, contentDescription = \"Swap\", modifier = Modifier.rotate(rotation))"
)
content = content.replace(
    "val temp = fromLang\n                            fromLang = toLang\n                            toLang = temp",
    "val temp = fromLang\n                            fromLang = toLang\n                            toLang = temp\n                            isSwapped = !isSwapped\n                            val t2 = text\n                            text = translatedText\n                            translatedText = t2"
)

# Add clipboard copy
content = content.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.draw.rotate\nimport android.content.ClipboardManager\nimport android.content.Context\nimport android.content.ClipData")

with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "w") as f:
    f.write(content)
