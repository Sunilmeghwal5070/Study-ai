import re

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "r") as f:
    content = f.read()

# Add import
content = content.replace("import com.example.api.RetrofitClient", "import com.example.api.RetrofitClient\nimport com.example.api.PicoRequest")

# 1. getSolverAnswer
old_getSolverAnswer = """        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                _error.value = "API Key is missing. Please configure it in Secrets."
                return
            }

            val parts = mutableListOf<Part>()
            val langInstruction = if (lang == "hi") "Please answer in Hindi." else "Please answer in English."
            var promptText = "Subject: $subject. "
            if (question.isNotEmpty()) {
                promptText += "Question: $question. "
            } else if (image != null) {
                promptText += "Analyze the provided image and solve the problem or describe it. "
            }
            promptText += langInstruction
            
            parts.add(Part(text = promptText))
            
            if (image != null) {
                parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = image.toBase64())))
            }

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = parts, role = "user")),
                systemInstruction = Content(parts = listOf(Part(text = "You are a helpful tutor. Provide a very short, direct, and simple final answer to the student without any complex formatting, markdown, or LaTeX symbols. Do not explain the steps, just give the final answer in plain text.")), role = "model")
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val answer = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text"""

new_getSolverAnswer = """        try {
            val langInstruction = if (lang == "hi") "Please answer in Hindi." else "Please answer in English."
            val sysPrompt = "You are a helpful tutor. Provide a very short, direct, and simple final answer to the student without any complex formatting, markdown, or LaTeX symbols. Do not explain the steps, just give the final answer in plain text.\\n"
            var promptText = sysPrompt + "Subject: $subject. "
            if (question.isNotEmpty()) {
                promptText += "Question: $question. "
            } else if (image != null) {
                promptText += "Analyze the provided image and solve the problem or describe it. "
            }
            promptText += langInstruction
            
            val request = PicoRequest(prompt = promptText)
            val response = RetrofitClient.picoService.generateContent("aero/run/llm-api?pk=v1-Z0FBQUFBQnBlNm5yQzU1anRkLTFkZHp4NDZESmVjakhCYXBBSVBEcEdHTnY3RGpmc3AxOFJ6MmNHTjdURHV0TExVNnN4VXl5a0d4Z2taeWZnYjNhcktHZjdzYUQwZEVlU1E9PQ==", request)
            val answer = response.text"""

content = content.replace(old_getSolverAnswer, new_getSolverAnswer)

# 2. explainAnswer
old_explainAnswer = """        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            val langInstruction = if (lang == "hi") "कृपया हिंदी में समझाएं।" else "Please explain in English."
            val prompt = "Explain this answer in simple steps for a student: \\"$answer\\". $langInstruction Provide a clear, step-by-step breakdown that helps understand the concept. Do NOT use any special LaTeX math symbols like $$ or markdown symbols. Use plain text formatting and simple structure so anyone can understand."
            
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val explanation = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text"""

new_explainAnswer = """        try {
            val langInstruction = if (lang == "hi") "कृपया हिंदी में समझाएं।" else "Please explain in English."
            val prompt = "Explain this answer in simple steps for a student: \\"$answer\\". $langInstruction Provide a clear, step-by-step breakdown that helps understand the concept. Do NOT use any special LaTeX math symbols like $$ or markdown symbols. Use plain text formatting and simple structure so anyone can understand."
            
            val request = PicoRequest(prompt = prompt)
            val response = RetrofitClient.picoService.generateContent("aero/run/llm-api?pk=v1-Z0FBQUFBQnBlNm5yQzU1anRkLTFkZHp4NDZESmVjakhCYXBBSVBEcEdHTnY3RGpmc3AxOFJ6MmNHTjdURHV0TExVNnN4VXl5a0d4Z2taeWZnYjNhcktHZjdzYUQwZEVlU1E9PQ==", request)
            val explanation = response.text"""

content = content.replace(old_explainAnswer, new_explainAnswer)

# 3. translateText
old_translateText = """        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            val prompt = "Translate the following text from $fromLang to $toLang: \\"$text\\". Only provide the translation, no explanations."
            
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val translation = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text"""

new_translateText = """        try {
            val prompt = "Translate the following text from $fromLang to $toLang: \\"$text\\". Only provide the translation, no explanations."
            
            val request = PicoRequest(prompt = prompt)
            val response = RetrofitClient.picoService.generateContent("aero/run/llm-api?pk=v1-Z0FBQUFBQnBlNm5yQzU1anRkLTFkZHp4NDZESmVjakhCYXBBSVBEcEdHTnY3RGpmc3AxOFJ6MmNHTjdURHV0TExVNnN4VXl5a0d4Z2taeWZnYjNhcktHZjdzYUQwZEVlU1E9PQ==", request)
            val translation = response.text"""
content = content.replace(old_translateText, new_translateText)

# 4. sendChatMessage
old_sendChatMessage = """        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            repository.insertChatMessage(ChatMessage(role = "user", content = message))
            deductCredit()
            
            val langContext = if (lang == "hi") "Reply in Hindi." else "Reply in English."
            val sysInstruction = "You are a helpful AI study assistant. $langContext"
            
            // Build conversation history (limit to last 10 messages)
            val currentHistory = chatMessages.value.takeLast(10).filter { it.id > 0 } // exclude pending
            val contents = currentHistory.map { msg ->
                val apiRole = if (msg.role == "ai") "model" else "user"
                Content(parts = listOf(Part(text = msg.content)), role = apiRole)
            }.toMutableList()
            
            // In case the flow hasn't updated yet, ensure the latest message is there
            if (contents.isEmpty() || contents.last().parts.firstOrNull()?.text != message) {
                contents.add(Content(parts = listOf(Part(text = message)), role = "user"))
            }
            
            val request = GenerateContentRequest(
                contents = contents,
                systemInstruction = Content(parts = listOf(Part(text = sysInstruction)), role = "model")
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text"""

new_sendChatMessage = """        try {
            repository.insertChatMessage(ChatMessage(role = "user", content = message))
            deductCredit()
            
            val langContext = if (lang == "hi") "Reply in Hindi." else "Reply in English."
            val sysInstruction = "You are a helpful AI study assistant. $langContext\\n"
            
            // Build conversation history (limit to last 10 messages)
            val currentHistory = chatMessages.value.takeLast(10).filter { it.id > 0 } // exclude pending
            var conversation = sysInstruction
            for (msg in currentHistory) {
                 val r = if (msg.role == "ai") "Assistant" else "User"
                 conversation += "$r: ${msg.content}\\n"
            }
            if (currentHistory.isEmpty() || currentHistory.last().content != message) {
                 conversation += "User: $message\\n"
            }
            conversation += "Assistant:"
            
            val request = PicoRequest(prompt = conversation)
            val response = RetrofitClient.picoService.generateContent("aero/run/llm-api?pk=v1-Z0FBQUFBQnBlNm5yQzU1anRkLTFkZHp4NDZESmVjakhCYXBBSVBEcEdHTnY3RGpmc3AxOFJ6MmNHTjdURHV0TExVNnN4VXl5a0d4Z2taeWZnYjNhcktHZjdzYUQwZEVlU1E9PQ==", request)
            val reply = response.text"""
content = content.replace(old_sendChatMessage, new_sendChatMessage)

with open("app/src/main/java/com/example/ui/StudyViewModel.kt", "w") as f:
    f.write(content)

