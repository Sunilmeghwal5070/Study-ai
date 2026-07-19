sed -i '121c\
    val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->\
        if (result.resultCode == android.app.Activity.RESULT_OK) {\
            val data = result.data\
            val results = data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)\
            if (!results.isNullOrEmpty()) {\
                question = results[0]\
            }\
        }\
    }' app/src/main/java/com/example/ui/screens/SolverScreen.kt
