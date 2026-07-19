import re

# SolverScreen
with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

old_tts = """IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(answer!!, lang, true) }) { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }"""
new_tts = """TtsIconButton(text = answer!!, lang = lang, voiceEnabled = true)"""
content = content.replace(old_tts, new_tts)
# also for AutoMirrored if it was updated
old_tts2 = """IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(answer!!, lang, true) }) { Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }"""
content = content.replace(old_tts2, new_tts)

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)

# TranslatorScreen
with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "r") as f:
    content = f.read()

old_tts3 = """IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(translatedText ?: "", if(toLang == "Hindi") "hi" else "en", true) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }"""
new_tts3 = """TtsIconButton(text = translatedText ?: "", lang = if(toLang == "Hindi") "hi" else "en", voiceEnabled = true)"""
content = content.replace(old_tts3, new_tts3)

old_tts4 = """IconButton(onClick = { com.example.ui.utils.MediaUtils.speak(translatedText ?: "", if(toLang == "Hindi") "hi" else "en", true) }) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }"""
content = content.replace(old_tts4, new_tts3)

with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "w") as f:
    f.write(content)
