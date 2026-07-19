import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

content = content.replace("Text(answer!!, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)", "Text(com.example.ui.utils.MarkdownUtils.parseMarkdown(answer!!), fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)")
content = content.replace("Text(explanation!!, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)", "Text(com.example.ui.utils.MarkdownUtils.parseMarkdown(explanation!!), fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)")

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
