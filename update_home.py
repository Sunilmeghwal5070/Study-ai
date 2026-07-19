import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Replace val credits
credits_replace = """    val credits = settings?.credits ?: 20
    val favoriteSubjects = settings?.favoriteSubjects?.split(",")?.filter { it.isNotEmpty() }?.toSet() ?: emptySet()
    
    val sortedSubjects = remember(favoriteSubjects) {
        subjects.sortedByDescending { favoriteSubjects.contains(it.id) }
    }"""
content = content.replace('    val credits = settings?.credits ?: 20', credits_replace)

# Replace items(subjects)
items_old = """        items(subjects) { subject ->
            SubjectCard(subject = subject, lang = lang) {
                navController.navigate(Screen.Solver.createRoute(subject.id, AppStrings.get(subject.nameKey, lang)))
            }
        }"""
items_new = """        items(sortedSubjects, key = { it.id }) { subject ->
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
        }"""
content = content.replace(items_old, items_new)

# Update SubjectCard signature and implementation
card_old = """@Composable
fun SubjectCard(subject: Subject, lang: String, onClick: () -> Unit) {
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
            .bounceClick { onClick() },
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 8.dp,
    ) {"""

card_new = """import androidx.compose.material.icons.filled.Star

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
    ) {"""
content = content.replace(card_old, card_new)

# Add Star icon if favorite
box_content_old = """                Box(modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).padding(8.dp)) {
                    Icon(subject.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }"""
box_content_new = """                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).padding(8.dp)) {
                        Icon(subject.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    if (isFavorite) {
                        Icon(Icons.Default.Star, contentDescription = "Favorite", tint = Color(0xFFFFD700), modifier = Modifier.size(24.dp))
                    }
                }"""
content = content.replace(box_content_old, box_content_new)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
