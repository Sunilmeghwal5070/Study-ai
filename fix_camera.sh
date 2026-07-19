sed -i '127,133c\
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->\
        if (isGranted) {\
            coroutineScope.launch { snackbarHostState.showSnackbar("Camera permission granted") }\
        } else {\
            coroutineScope.launch { snackbarHostState.showSnackbar("Camera permission denied") }\
        }\
    }' app/src/main/java/com/example/ui/screens/SolverScreen.kt
