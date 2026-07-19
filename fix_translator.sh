sed -i 's/enabled = !isLoading && text.isNotBlank(),/enabled = text.isNotBlank(),\n                        isLoading = isLoading,/g' app/src/main/java/com/example/ui/screens/TranslatorScreen.kt
sed -i '/if (isLoading) {/,/}/d' app/src/main/java/com/example/ui/screens/TranslatorScreen.kt
