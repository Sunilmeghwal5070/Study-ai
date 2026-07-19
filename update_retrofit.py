import re

# Update GeminiApiService.kt
with open("app/src/main/java/com/example/api/GeminiApiService.kt", "r") as f:
    api_content = f.read()

api_content = api_content.replace('import retrofit2.http.POST', 'import retrofit2.http.POST\nimport retrofit2.http.Url')
api_content = api_content.replace(
    '@POST("v1beta/models/gemini-1.5-flash:generateContent")\n    suspend fun generateContent(\n        @Query("key") apiKey: String,',
    '@POST\n    suspend fun generateContent(\n        @Url url: String,\n        @Query("key") apiKey: String,'
)

with open("app/src/main/java/com/example/api/GeminiApiService.kt", "w") as f:
    f.write(api_content)

# Update StudyViewModel.kt
with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "r") as f:
    vm_content = f.read()

vm_content = vm_content.replace('RetrofitClient.service.generateContent(apiKey, request)', 'RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)')

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "w") as f:
    f.write(vm_content)
