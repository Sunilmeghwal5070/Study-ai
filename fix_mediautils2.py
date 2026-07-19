import re

with open("app/src/main/java/com/example/ui/utils/MediaUtils.kt", "r") as f:
    content = f.read()

content = content.replace("if (!voiceEnabled) return", "if (!voiceEnabled) return null")
content = content.replace("val ttsInstance = tts ?: return", "val ttsInstance = tts ?: return null")
content = content.replace("if (!isTtsReady.value) return", "if (!isTtsReady.value) return null")
content = content.replace("ttsInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)", 'val utteranceId = "utterance_${System.currentTimeMillis()}"\n        ttsInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)\n        return utteranceId')

with open("app/src/main/java/com/example/ui/utils/MediaUtils.kt", "w") as f:
    f.write(content)
