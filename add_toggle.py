with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "r") as f:
    content = f.read()

toggle_fn = """    fun toggleFavoriteSubject(subjectId: String) {
        val currentSettings = userSettings.value ?: return
        val currentFavorites = currentSettings.favoriteSubjects.split(",").filter { it.isNotEmpty() }.toMutableSet()
        if (currentFavorites.contains(subjectId)) {
            currentFavorites.remove(subjectId)
        } else {
            currentFavorites.add(subjectId)
        }
        val newFavoritesStr = currentFavorites.joinToString(",")
        saveSettings(currentSettings.copy(favoriteSubjects = newFavoritesStr))
    }

    fun completeSetup"""

content = content.replace("    fun completeSetup", toggle_fn)

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "w") as f:
    f.write(content)
