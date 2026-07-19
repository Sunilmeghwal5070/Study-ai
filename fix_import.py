import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.material.icons.filled.Star\n\n@Composable\nfun SubjectCard", "@Composable\nfun SubjectCard")

if "import androidx.compose.material.icons.filled.Star" not in content:
    content = content.replace("import androidx.compose.material.icons.filled.AutoAwesome", "import androidx.compose.material.icons.filled.AutoAwesome\nimport androidx.compose.material.icons.filled.Star")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
