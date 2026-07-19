import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "contract = com.canhub.cropper.CropImageContract()) { uri: Uri? ->",
    "contract = com.canhub.cropper.CropImageContract()) { result ->\n        val uri = result.uriContent"
)

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
