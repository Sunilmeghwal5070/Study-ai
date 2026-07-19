import re

def update_file(filename):
    with open(filename, "r") as f:
        content = f.read()

    # Add Toast import if not exists
    if "import android.widget.Toast" not in content:
        content = content.replace("import android.content.Context", "import android.content.Context\nimport android.widget.Toast")
        
    # Replace clipboard copy with Toast
    # Find clipboard.setPrimaryClip(...)
    content = re.sub(
        r'(clipboardManager\.setPrimaryClip\(.*?\)|clipboard\.setPrimaryClip\(.*?\))',
        r'\1\n                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()',
        content
    )
    
    # Also add TtsIconButton import if not exists
    if "import com.example.ui.components.TtsIconButton" not in content and filename != "app/src/main/java/com/example/ui/screens/HistoryScreen.kt":
        content = content.replace("import androidx.compose.runtime.*", "import androidx.compose.runtime.*\nimport com.example.ui.components.TtsIconButton")

    with open(filename, "w") as f:
        f.write(content)

update_file("app/src/main/java/com/example/ui/screens/SolverScreen.kt")
update_file("app/src/main/java/com/example/ui/screens/ChatScreen.kt")
update_file("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt")
update_file("app/src/main/java/com/example/ui/screens/HistoryScreen.kt")
