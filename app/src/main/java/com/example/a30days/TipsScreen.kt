package com.example.a30days

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a30days.model.Tip
import com.example.a30days.model.TipsRepository
import com.example.a30days.ui.theme._30daysTheme

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo container
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Using a placeholder icon as I cannot verify if the logo was successfully moved to drawable
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize(),
                tint = Color.Red
            )
        }
        Text(
            text = "Welcome to 30 Days UI/UX",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Master the art of design, one day at a time.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun TipsApp(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    var showWelcome by remember { mutableStateOf(true) }
    val favoriteTips = remember { mutableStateListOf<Tip>() }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    Crossfade(targetState = showWelcome, label = "WelcomeTransition") { isWelcome ->
        if (isWelcome) {
            WelcomeScreen(onContinue = { showWelcome = false })
        } else {
            Scaffold(
                topBar = {
                    TipsTopAppBar(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = onThemeToggle,
                        showFavoritesOnly = showFavoritesOnly,
                        onToggleFavorites = { showFavoritesOnly = !showFavoritesOnly }
                    )
                }
            ) { padding ->
                val displayedTips = if (showFavoritesOnly) favoriteTips else TipsRepository.tips

                Crossfade(targetState = showFavoritesOnly, label = "ScreenTransition") { isShowingFavorites ->
                    if (isShowingFavorites && favoriteTips.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No favorites yet!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        TipsList(
                            tips = displayedTips,
                            favoriteTips = favoriteTips,
                            onToggleFavorite = { tip ->
                                if (favoriteTips.contains(tip)) {
                                    favoriteTips.remove(tip)
                                    // Remove from persistent favorites if needed
                                } else {
                                    favoriteTips.add(tip)
                                }
                            },
                            contentPadding = padding
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsTopAppBar(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    showFavoritesOnly: Boolean,
    onToggleFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (showFavoritesOnly) "Favorite Tips" else stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onToggleFavorites) {
                Icon(
                    imageVector = if (showFavoritesOnly) Icons.AutoMirrored.Filled.List else Icons.Filled.Favorite,
                    contentDescription = if (showFavoritesOnly) "Show All" else "Show Favorites",
                    tint = if (showFavoritesOnly) MaterialTheme.colorScheme.primary else Color.Red
                )
            }
        },
        actions = {
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.Nightlight,
                    contentDescription = "Toggle Theme"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun TipsList(
    tips: List<Tip>,
    favoriteTips: SnapshotStateList<Tip>,
    onToggleFavorite: (Tip) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(tips, key = { it.day }) { tip ->
            TipItem(
                tip = tip,
                isFavorite = favoriteTips.contains(tip),
                onToggleFavorite = { onToggleFavorite(tip) },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun TipItem(
    tip: Tip,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val favoriteColor by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else MaterialTheme.colorScheme.outline,
        label = "FavoriteColorAnimation"
    )

    // Burst/Scale animation state
    val scale = remember { Animatable(1f) }

    // Trigger burst animation when isFavorite changes to true
    LaunchedEffect(isFavorite) {
        if (isFavorite) {
            scale.animateTo(
                targetValue = 1.6f,
                animationSpec = tween(durationMillis = 100)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        } else {
            scale.snapTo(1f)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.day, tip.day),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = favoriteColor,
                        modifier = Modifier.graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value
                        )
                    )
                }
            }
            Text(
                text = stringResource(tip.titleRes),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(tip.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(tip.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipItemPreview() {
    _30daysTheme {
        TipItem(
            tip = Tip(1, R.string.tip1_title, R.string.tip1_desc, R.drawable.ic_launcher_foreground),
            isFavorite = false,
            onToggleFavorite = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipsAppPreview() {
    _30daysTheme {
        TipsApp(isDarkTheme = false, onThemeToggle = {})
    }
}
