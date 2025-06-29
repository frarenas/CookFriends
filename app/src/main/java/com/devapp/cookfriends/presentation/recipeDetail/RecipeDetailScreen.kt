package com.devapp.cookfriends.presentation.recipeDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
    navigateBack: () -> Boolean
) {
    val viewModel: RecipeDetailViewModel = hiltViewModel()
    val state by viewModel.recipeDetailState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }
    val exampleRecipe = Recipe(
        name = state.recipe?.name,
        portions = 4,
        ingredients = listOf(),
        steps = listOf(
            Step(
                order = 1,
                content = "descripcion paso 1.",
                recipeId = Uuid.random(),
                photos = listOf(StepPhoto(Uuid.random(),"url",Uuid.random()))
            ),
            Step(
                order = 2,
                content = "descripcion paso 1.",
                recipeId = Uuid.random(),
                photos = listOf(StepPhoto(Uuid.random(),"url",Uuid.random()))
            )
        )
    )

    val comments = listOf(
        Comment(
            id = Uuid.random(),
            comment = "sample Text",
            userId = Uuid.random(),
            recipeId = Uuid.random(),
        )
    );

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(state.recipe?.name.toString()) },
                navigationIcon = {
                    IconButton(onClick = { /*navHostController.popBackStack()*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    Text("5.0", color = Color(255,165,52))
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            tint = Color(255,165,52),
                            contentDescription = "Valorar"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Agregar a favoritos"
                        )
                    }
                }

        )}
    ){
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            recipeHeader()
            ingredientsSection(exampleRecipe.ingredients)
            HorizontalDivider()
            stepsSection(exampleRecipe.steps)
            HorizontalDivider()
            commentSection(comments)
        }
    }
}

@Composable
fun recipeHeader(){
    AsyncImage(
        model = "https://www.clarin.com/2024/03/12/1jgNm90_r_340x340__1.jpg",
        contentDescription = "foto portada de la receta"
    )
}

@Composable
fun ingredientsSection(ingredients: List<Ingredient>) {
    Text("Ingredientes", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
    Row (modifier = Modifier.fillMaxWidth()){
        Column {
            ingredients.forEach(){
                i->Row(){
                    Text(i.name.toString())
                }
            }
        }
        Column(modifier = Modifier.padding(start = 20.dp )){
            ingredients.forEach(){
                    i->Row(){
                Text(i.quantity.toString())
                Text(i.measurement.toString())
            }
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonColors(Color.LightGray, Color.Black, Color.LightGray, Color.Gray),
                onClick = { onEditProportions() }) {
                Text("Calcular proporciones")
            }
        }
    }
}

@Composable
fun stepsSection(steps: List<Step>){
    Column {
        Text("Preparacion", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        steps.forEach(){
            s -> Row {
                Column(){
                    AsyncImage(
                        model = "https://www.clarin.com/2024/03/12/1jgNm90_r_340x340__1.jpg",
                        contentDescription = "foto portada de la receta",
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                }
                Column(Modifier.padding(start = 10.dp )) {
                    Text("Paso "+s.order, fontSize = 18.sp)
                    Text(s.content.toString())
                }
        }}
    }
}

@Composable
fun commentSection(comments: List<Comment>){
    Column {
        Text("Comentarios", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        // Campo para ingresar nuevos comentarios
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Escribe un comentario") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onNewComment("Texto del comentario") }
            ) {
            Text("Enviar")
        }

        // Lista de comentarios existentes
        LazyColumn {
            items(comments) { comment ->
                commentItem(comment)
            }
        }
    }
}



@Composable
fun commentItem(comment: Comment){

    Column {
        Text("UserName1")
        Text("Sample text")
        HorizontalDivider()
    }
}

fun onNewComment(s: String) {
    println("saving comment for the recipe")
}

fun onEditProportions(){
    println("modifying recipe proportions")
}