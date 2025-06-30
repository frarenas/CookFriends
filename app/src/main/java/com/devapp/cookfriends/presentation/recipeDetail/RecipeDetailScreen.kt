package com.devapp.cookfriends.presentation.recipeDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.devapp.cookfriends.domain.model.Comment
import com.devapp.cookfriends.domain.model.Ingredient
import com.devapp.cookfriends.domain.model.Recipe
import com.devapp.cookfriends.domain.model.Step
import com.devapp.cookfriends.domain.model.StepPhoto
import kotlin.uuid.Uuid


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Uuid,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.recipeDetailState.collectAsState()
    val recipe = state.recipe

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.name ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Text(recipe?.averageRating?.toString() ?: "-", color = Color(0xFFFFA534))
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Star, tint = Color(0xFFFFA534), contentDescription = "Valorar")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Agregar a favoritos")
                    }
                },
                windowInsets = WindowInsets(0.dp)
            )
        }
    ) { innerPadding ->
        if (recipe != null) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item {
                    recipeHeader(recipe.recipePhotos.firstOrNull()?.url)
                    ingredientsSection(recipe.ingredients)
                    HorizontalDivider()
                    stepsSection(recipe.steps)
                    HorizontalDivider()
                    Text(
                        "Comentarios",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    commentInputSection()
                    HorizontalDivider()
                }
                items(recipe.comments) { comment ->
                    commentItem(comment)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun recipeHeader(imageUrl: String?) {
    AsyncImage(
        model = imageUrl ?: "",
        contentDescription = "foto portada de la receta",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun ingredientsSection(ingredients: List<Ingredient>) {
    Text("Ingredientes", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {
            ingredients.forEach { i ->
                Row {
                    Text(i.name ?: "")
                }
            }
        }
        Column(modifier = Modifier.padding(start = 20.dp)) {
            ingredients.forEach { i ->
                Row {
                    Text(i.quantity.toString())
                    Text(" ${i.measurement}")
                }
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { /* TODO: Calcular proporciones */ }
            ) {
                Text("Calcular proporciones")
            }
        }
    }
}

@Composable
fun stepsSection(steps: List<Step>) {
    Column {
        Text("Preparación", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        steps.forEach { s ->
            Row {
                AsyncImage(
                    model = s.photos.firstOrNull()?.url ?: "",
                    contentDescription = "Foto del paso",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
                Column(Modifier.padding(start = 10.dp)) {
                    Text("Paso ${s.order}", fontSize = 18.sp)
                    Text(s.content ?: "")
                }
            }
        }
    }
}

@Composable
fun commentInputSection() {
    var newComment by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = newComment,
            onValueChange = { newComment = it },
            label = { Text("Escribe un comentario") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { /* TODO: enviar comentario */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }
    }
}
@Composable
fun commentItem(comment: Comment) {
    Column {
        Text(comment.userId.toString() ?: "Usuario")
        Text(comment.comment ?: "")
        HorizontalDivider()
    }
}

fun onNewComment(s: String) {
    println("saving comment for the recipe")
}

fun onEditProportions(){
    println("modifying recipe proportions")
}