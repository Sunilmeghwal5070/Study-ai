sed -i '/val cameraPermissionLauncher = rememberLauncherForActivityResult/,/    }/d' app/src/main/java/com/example/ui/screens/SolverScreen.kt
sed -i 's/cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)//g' app/src/main/java/com/example/ui/screens/SolverScreen.kt
