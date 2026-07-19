import re

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "r") as f:
    content = f.read()

# getSolverAnswer
old_lang1 = 'val langInstruction = if (lang == "hi") "Please answer in Hindi." else "Please answer in English."'
new_lang1 = 'val langInstruction = if (lang == "hi") "Regardless of the language I asked in, you MUST answer entirely in Hindi." else "Regardless of the language I asked in, you MUST answer entirely in English."'
content = content.replace(old_lang1, new_lang1)

# explainAnswer
old_lang2 = 'val langInstruction = if (lang == "hi") "कृपया हिंदी में समझाएं।" else "Please explain in English."'
new_lang2 = 'val langInstruction = if (lang == "hi") "Regardless of the language I asked in, you MUST explain entirely in Hindi." else "Regardless of the language I asked in, you MUST explain entirely in English."'
content = content.replace(old_lang2, new_lang2)

# sendChatMessage
old_lang3 = 'val langContext = if (lang == "hi") "Reply in Hindi." else "Reply in English."'
new_lang3 = 'val langContext = if (lang == "hi") "Regardless of the user\'s language, you MUST reply entirely in Hindi." else "Regardless of the user\'s language, you MUST reply entirely in English."'
content = content.replace(old_lang3, new_lang3)

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "w") as f:
    f.write(content)
