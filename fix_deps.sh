sed -i '/\[versions\]/a canhub = "4.5.0"' gradle/libs.versions.toml
sed -i '/\[libraries\]/a canhub-cropper = { group = "com.vanniktech", name = "android-image-cropper", version.ref = "canhub" }' gradle/libs.versions.toml
