import re

with open("app/src/main/java/com/example/api/GeminiApiService.kt", "r") as f:
    content = f.read()

pico_service = """    val picoService: PicoApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://backend.buildpicoapps.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PicoApiService::class.java)
    }
}"""

content = content.replace("    }\n}", "    }\n\n" + pico_service)

with open("app/src/main/java/com/example/api/GeminiApiService.kt", "w") as f:
    f.write(content)
