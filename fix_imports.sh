for file in app/src/main/java/com/example/ui/screens/SolverScreen.kt app/src/main/java/com/example/ui/screens/TranslatorScreen.kt; do
  sed -i 's/package com.example.ui.screens/package com.example.ui.screens\n\nimport com.example.ui.components.PrimaryButton/g' "$file"
done
