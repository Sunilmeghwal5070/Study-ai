import re

# TranslatorScreen
with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "r") as f:
    content = f.read()

old_tts5 = """IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(translatedText ?: "", if(toLang == "Hindi") "hi" else "en", true) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                            }"""
new_tts5 = """TtsIconButton(text = translatedText ?: "", lang = if(toLang == "Hindi") "hi" else "en", voiceEnabled = true)"""
content = content.replace(old_tts5, new_tts5)

old_tts6 = """IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(translatedText ?: "", if(toLang == "Hindi") "hi" else "en", true) }) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                            }"""
content = content.replace(old_tts6, new_tts5)

with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "w") as f:
    f.write(content)
