import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

content = content.replace("    /*\\n        onDispose { tts.shutdown() }\\n    }", "    ")
content = content.replace("    /*\n        onDispose { tts?.shutdown() }\n    }", "    ")
with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
