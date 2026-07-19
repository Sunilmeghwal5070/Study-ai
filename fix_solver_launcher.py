import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

# Make sure CropImageContractOptions uses CropImageOptions properly
if "imagePickerLauncher.launch(com.canhub.cropper.CropImageContractOptions(uri = null, cropImageOptions = com.canhub.cropper.CropImageOptions(imageSourceIncludeGallery = true, imageSourceIncludeCamera = true)))" in content:
    content = content.replace(
        "imagePickerLauncher.launch(com.canhub.cropper.CropImageContractOptions(uri = null, cropImageOptions = com.canhub.cropper.CropImageOptions(imageSourceIncludeGallery = true, imageSourceIncludeCamera = true)))",
        "imagePickerLauncher.launch(CropImageContractOptions(uri = null, cropImageOptions = CropImageOptions(imageSourceIncludeGallery = true, imageSourceIncludeCamera = true)))"
    )

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
