import re

with open("app/src/main/java/com/example/ui/screens/HistoryScreen.kt", "r") as f:
    content = f.read()

content = content.replace("Text(selectedItem.value!!.answer)", "Text(com.example.ui.utils.MarkdownUtils.parseMarkdown(selectedItem.value!!.answer))")

with open("app/src/main/java/com/example/ui/screens/HistoryScreen.kt", "w") as f:
    f.write(content)
