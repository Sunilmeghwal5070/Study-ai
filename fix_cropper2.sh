sed -i '103,113c\
    val imagePickerLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->\
        if (result.isSuccessful) {\
            imageUri = result.uriContent\
            imageUri?.let {\
                bitmap = if (Build.VERSION.SDK_INT < 28) {\
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)\
                } else {\
                    val source = ImageDecoder.createSource(context.contentResolver, it)\
                    ImageDecoder.decodeBitmap(source)\
                }\
            }\
        } else {\
            val error = result.error\
            if (error != null) {\
                coroutineScope.launch { snackbarHostState.showSnackbar("Cropping failed: ${error.message}") }\
            }\
        }\
    }' app/src/main/java/com/example/ui/screens/SolverScreen.kt
