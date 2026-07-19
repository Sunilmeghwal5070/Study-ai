import re

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "r") as f:
    content = f.read()

# Replace micPermissionLauncher definition
old_launcher = """val micPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            coroutineScope.launch { snackbarHostState.showSnackbar("Mic permission granted") }
        } else {
            coroutineScope.launch { snackbarHostState.showSnackbar("Mic permission denied") }
        }
    }"""

new_launcher = """val speechRecognizerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)
            val spokenText = data?.get(0) ?: ""
            if (spokenText.isNotEmpty()) {
                question = spokenText
            }
        }
    }"""

content = content.replace(old_launcher, new_launcher)

# Replace the onClick
content = content.replace(
    "micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)",
    """try {
                                    val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    }
                                    speechRecognizerLauncher.launch(intent)
                                } catch (e: Exception) {
                                    coroutineScope.launch { snackbarHostState.showSnackbar("Speech recognition not supported") }
                                }"""
)

with open("app/src/main/java/com/example/ui/screens/SolverScreen.kt", "w") as f:
    f.write(content)
