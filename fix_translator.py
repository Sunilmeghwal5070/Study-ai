import re

with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "r") as f:
    content = f.read()

# Fix the duplicate rotation declarations
content = re.sub(r'var isSwapped by remember \{ mutableStateOf\(false\) \}\s*val rotation by androidx.compose.animation.core.animateFloatAsState\(targetValue = if \(isSwapped\) 180f else 0f\)\s*IconButton\(onClick = \{ /\* Handle copy \*/ \}\)', 'IconButton(onClick = { /* Handle copy */ })', content)

content = re.sub(r'var isSwapped by remember \{ mutableStateOf\(false\) \}\s*val rotation by androidx.compose.animation.core.animateFloatAsState\(targetValue = if \(isSwapped\) 180f else 0f\)\s*IconButton\(onClick = \{ com.example.ui.utils.MediaUtils.speak', 'IconButton(onClick = { com.example.ui.utils.MediaUtils.speak', content)

# Now apply rotation only to the swap button
swap_button_old = """IconButton(
                    onClick = {
                        val tempLang = fromLang
                        fromLang = toLang
                        toLang = tempLang
                        
                        val tempText = text
                        text = translatedText ?: ""
                        translatedText = if (tempText.isNotBlank()) tempText else null
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Swap", tint = Color.White)
                }"""

swap_button_new = """var isSwapped by remember { mutableStateOf(false) }
                val rotation by androidx.compose.animation.core.animateFloatAsState(targetValue = if (isSwapped) 180f else 0f)
                IconButton(
                    onClick = {
                        isSwapped = !isSwapped
                        val tempLang = fromLang
                        fromLang = toLang
                        toLang = tempLang
                        
                        val tempText = text
                        text = translatedText ?: ""
                        translatedText = if (tempText.isNotBlank()) tempText else null
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Swap", tint = Color.White, modifier = Modifier.rotate(rotation))
                }"""

content = content.replace(swap_button_old, swap_button_new)

# Add clipboard copy functionality to Handle copy
content = content.replace(
    "IconButton(onClick = { /* Handle copy */ }) {",
    """IconButton(onClick = {
                                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Translated Text", translatedText ?: "")
                                clipboardManager.setPrimaryClip(clip)
                            }) {"""
)

with open("app/src/main/java/com/example/ui/screens/TranslatorScreen.kt", "w") as f:
    f.write(content)
