import re

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "suspend fun sendChatMessage(message: String, lang: String) {",
    "suspend fun sendChatMessage(message: String, lang: String, onSuccess: (String) -> Unit = {}) {"
)
content = content.replace(
    "if (reply != null) {\n                repository.insertChatMessage(ChatMessage(role = \"ai\", content = reply))",
    "if (reply != null) {\n                repository.insertChatMessage(ChatMessage(role = \"ai\", content = reply))\n                onSuccess(reply)"
)

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "w") as f:
    f.write(content)
