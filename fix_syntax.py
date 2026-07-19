import os

files = [
    "app/src/main/java/com/example/ui/screens/SolverScreen.kt",
    "app/src/main/java/com/example/ui/screens/ChatScreen.kt",
    "app/src/main/java/com/example/ui/screens/TranslatorScreen.kt",
    "app/src/main/java/com/example/ui/screens/HistoryScreen.kt"
]

for filename in files:
    with open(filename, "r") as f:
        content = f.read()
        
    # Find something like:
    # clipboard.setPrimaryClip(ClipData.newPlainText("Answer", answer!!)
    # Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show())
    # And fix the parenthesis
    
    content = content.replace(")\n                                Toast.makeText(context, \"Copied to clipboard\", Toast.LENGTH_SHORT).show())", "))\n                                Toast.makeText(context, \"Copied to clipboard\", Toast.LENGTH_SHORT).show()")
    content = content.replace(")\n                                    Toast.makeText(context, \"Copied to clipboard\", Toast.LENGTH_SHORT).show())", "))\n                                    Toast.makeText(context, \"Copied to clipboard\", Toast.LENGTH_SHORT).show()")
    content = content.replace("clip\n                                Toast.makeText(context, \"Copied to clipboard\", Toast.LENGTH_SHORT).show())", "clip)\n                                Toast.makeText(context, \"Copied to clipboard\", Toast.LENGTH_SHORT).show()")
    
    with open(filename, "w") as f:
        f.write(content)
