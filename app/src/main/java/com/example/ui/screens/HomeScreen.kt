package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import com.example.ui.StudyViewModel
import com.example.ui.navigation.Screen
import com.example.ui.utils.AppStrings
import com.example.ui.utils.bounceClick
import com.example.ui.components.CreditRefreshTimer

@Composable
fun HomeScreen(navController: NavController, viewModel: StudyViewModel, innerPadding: PaddingValues) {
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings?.language ?: "en"
    val name = settings?.name ?: AppStrings.get("student", lang)
    val role = settings?.userClass ?: AppStrings.get("ai_study_assistant", lang)
    val credits = settings?.credits ?: 20
    val favoriteSubjects = settings?.favoriteSubjects?.split(",")?.filter { it.isNotEmpty() }?.toSet() ?: emptySet()
    
    val sortedSubjects = remember(favoriteSubjects) {
        subjects.sortedByDescending { favoriteSubjects.contains(it.id) }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            // User Welcome Header
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "${AppStrings.get("hello", lang)}, $name!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text(text = role, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f), maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            }
                        }
                        
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$credits",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                // Spacer(modifier = Modifier.height(8.dp))
                // CreditRefreshTimer(lang = lang)
            }
        }
        
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(AppStrings.get("select_subject", lang), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }

        items(sortedSubjects, key = { it.id }) { subject ->
            val isFavorite = favoriteSubjects.contains(subject.id)
            SubjectCard(
                subject = subject, 
                lang = lang,
                isFavorite = isFavorite,
                onLongClick = { viewModel.toggleFavoriteSubject(subject.id) },
                onClick = {
                    navController.navigate(Screen.Solver.createRoute(subject.id, AppStrings.get(subject.nameKey, lang)))
                }
            )
        }
    }
}

@Composable
fun SubjectCard(
    subject: Subject, 
    lang: String, 
    isFavorite: Boolean = false,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(500),
        label = "alpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
            .bounceClick(onLongClick = onLongClick, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(subject.color1, subject.color2)))
                .padding(16.dp)
        ) {
            Icon(
                imageVector = subject.icon,
                contentDescription = AppStrings.get(subject.nameKey, lang),
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 16.dp, y = 16.dp)
            )
            
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).padding(8.dp)) {
                        Icon(subject.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    if (isFavorite) {
                        Icon(Icons.Default.Star, contentDescription = "Favorite", tint = Color(0xFFFFD700), modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = AppStrings.get(subject.nameKey, lang),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}
