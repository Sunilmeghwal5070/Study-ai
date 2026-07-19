package com.example.ui.screens

import com.example.ui.components.PrimaryButton

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.Locale
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.VolumeUp
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.ui.utils.bounceClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.StudyViewModel
import com.example.ui.utils.AppStrings
import kotlinx.coroutines.launch

@Composable
fun SolverScreen(
    navController: NavController,
    viewModel: StudyViewModel,
    subjectId: String,
    subjectName: String,
    innerPadding: PaddingValues
) {
    var question by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var answer by remember { mutableStateOf<String?>(null) }
    var explanation by remember { mutableStateOf<String?>(null) }
    
    val settings by viewModel.userSettings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val credits = settings?.credits ?: 0
    var lang by remember(settings?.language) { mutableStateOf(settings?.language ?: "en") }
    var showSubscriptionDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tts = remember(context) { 
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = if (lang == "hi") Locale("hi", "IN") else Locale.US
            }
        }
        textToSpeech
    }
    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    if (showSubscriptionDialog) {
        com.example.ui.components.SubscriptionDialog(lang = lang,
            onDismiss = { showSubscriptionDialog = false },
            onPurchase = { amount ->
                viewModel.addCredits(amount)
                showSubscriptionDialog = false
                coroutineScope.launch { snackbarHostState.showSnackbar("Purchased $amount credits!") }
            }
        )
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = com.canhub.cropper.CropImageContract()) { result ->
        val uri = result.uriContent
        imageUri = uri
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
            // Auto OCR feature is skipped in pure Compose, we just pass image to Gemini directly.
        }
    }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)
            val spokenText = data?.get(0) ?: ""
            if (spokenText.isNotEmpty()) {
                question = spokenText
            }
        }
    }
    

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error!!)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "$subjectName Solver",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.secondary
                ) {
                    Text(
                        text = "$credits Credits",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = 16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            // Input Area
            Surface(
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(AppStrings.get("enter_question", lang), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        placeholder = { Text(AppStrings.get("type_question", lang)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = {
                            IconButton(onClick = { try {
                                    val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    }
                                    speechRecognizerLauncher.launch(intent)
                                } catch (e: Exception) {
                                    coroutineScope.launch { snackbarHostState.showSnackbar("Speech recognition not supported") }
                                } }) {
                                Icon(Icons.Default.Mic, contentDescription = "Mic")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (bitmap == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .bounceClick {
                                    
                                    imagePickerLauncher.launch(CropImageContractOptions(uri = null, cropImageOptions = CropImageOptions(imageSourceIncludeGallery = true, imageSourceIncludeCamera = true))) 
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Upload", tint = MaterialTheme.colorScheme.primary)
                                Text(AppStrings.get("tap_upload", lang), color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                contentDescription = "Uploaded image",
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { 
                                    bitmap = null
                                    imageUri = null
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(Color.Red, shape = RoundedCornerShape(50))
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Remove", tint = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Language Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = lang == "en",
                    onClick = { lang = "en" },
                    label = { Text(AppStrings.get("english", lang)) },
                    modifier = Modifier.padding(end = 16.dp)
                )
                FilterChip(
                    selected = lang == "hi",
                    onClick = { lang = "hi" },
                    label = { Text(AppStrings.get("hindi", lang)) }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                onClick = {
                    if (credits > 0) {
                        coroutineScope.launch {
                            viewModel.getSolverAnswer(subjectId, question, bitmap, lang) {
                                answer = it
                                explanation = null
                            }
                        }
                    } else {
                        showSubscriptionDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = (question.isNotBlank() || bitmap != null),
                isLoading = isLoading,
                brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary))
            ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.get("get_ai_answer", lang), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Result Area
            if (answer != null) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(AppStrings.get("ai_answer", lang), fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
                            IconButton(onClick = { tts?.speak(answer!!, TextToSpeech.QUEUE_FLUSH, null, null) }) { Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Answer", answer!!))
                            }) { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                            IconButton(onClick = {
                                val sendIntent = Intent().apply { action = Intent.ACTION_SEND; putExtra(Intent.EXTRA_TEXT, answer!!); type = "text/plain" }
                                context.startActivity(Intent.createChooser(sendIntent, null))
                            }) { Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                            PrimaryButton(
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.explainAnswer(answer!!, lang) {
                                            explanation = it
                                        }
                                    }
                                },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(AppStrings.get("explain", lang))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = answer!!,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        )

                        if (explanation != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(AppStrings.get("explanation", lang), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = explanation!!,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
