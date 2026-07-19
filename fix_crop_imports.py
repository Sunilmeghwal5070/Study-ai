import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

if "import com.canhub.cropper.CropImageContractOptions" not in content:
    content = content.replace(
        "import androidx.compose.ui.unit.dp",
        "import androidx.compose.ui.unit.dp\nimport com.canhub.cropper.CropImageContractOptions\nimport com.canhub.cropper.CropImageOptions"
    )

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
