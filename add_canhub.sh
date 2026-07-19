sed -i '/dependencies {/a \    implementation("com.vanniktech:android-image-cropper:4.5.0")' app/build.gradle.kts
sed -i 's/contract = ActivityResultContracts.GetContent()/contract = com.canhub.cropper.CropImageContract()/g' app/src/main/java/com/example/ui/screens/SolverScreen.kt
