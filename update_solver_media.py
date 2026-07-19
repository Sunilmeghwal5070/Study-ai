import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

# Replace TextToSpeech init
tts_init = """var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = if (lang == "hi") Locale("hi", "IN") else Locale.US
            }
        }
        textToSpeech"""
content = content.replace(tts_init, "null // removed tts")
content = content.replace("var tts by remember { mutableStateOf<TextToSpeech?>(null) }", "")
content = content.replace("DisposableEffect(Unit) {", "DisposableEffect(Unit) {\n        onDispose { com.example.ui.utils.MediaUtils.stopSpeaking() }\n    }\n    /*")
content = content.replace("onDispose { tts?.shutdown() }", "*/")

# Replace viewModel.getSolverAnswer call to include vibrate and speak
old_get_answer = """viewModel.getSolverAnswer(subjectId, question, bitmap, lang) {
                                answer = it
                                explanation = null
                            }"""
new_get_answer = """viewModel.getSolverAnswer(subjectId, question, bitmap, lang) {
                                answer = it
                                explanation = null
                                com.example.ui.utils.MediaUtils.vibrate(context, settings?.vibrationEnabled == true)
                                com.example.ui.utils.MediaUtils.speak(it, lang, settings?.voiceEnabled == true)
                            }"""
content = content.replace(old_get_answer, new_get_answer)

# Replace tts?.speak on IconButton
content = content.replace("tts?.speak(answer!!, TextToSpeech.QUEUE_FLUSH, null, null)", "com.example.ui.utils.MediaUtils.speak(answer!!, lang, true)")
content = content.replace("tts?.speak(explanation!!, TextToSpeech.QUEUE_FLUSH, null, null)", "com.example.ui.utils.MediaUtils.speak(explanation!!, lang, true)")

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
