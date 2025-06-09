package com.devapp.cookfriends.presentation.recipeDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.devapp.cookfriends.domain.models.Ingredient
import com.devapp.cookfriends.domain.models.Recipe
import com.devapp.cookfriends.domain.models.Step

@OptIn(ExperimentalMaterial3Api::class)

@Composable
//fun RecipeDetailScreen(viewModel: RecipeDetailViewModel = hiltViewModel()){
fun RecipeDetailScreen(navHostController: NavHostController){

    val exampleRecipe = Recipe(
        name = "Ravioles",
        author = "Chef Giovanni",
        type = "Italian",
        rate = 5.0,
        favorite = false,
        portions = 4,
        ingredients = listOf(
            Ingredient("Agua","2", "lts."),
            Ingredient("Harina", "1", "Kg."),
            Ingredient("Sal", "1", "crd.")),
        steps = listOf(
            Step(1, "descripcion paso 1."),
            Step(2, "descripcion paso 2."),
            Step(3, "descripcion paso 3"),
        )
    )

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(exampleRecipe.name) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
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
            stepsSection(exampleRecipe.steps)

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
    Text("Ingredientes")
    ingredients.forEach(){
        i->
            Row {
                Text(i.name.toString())
                Text(i.quantity.toString())
                Text(i.measurement.toString())
            }
    }
}

@Composable
fun stepsSection(steps: List<Step>){
    Column {
        Text("Preparacion")
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
                Column {
                    Text("Paso "+s.order)
                    Text(s.content.toString())
                }
        }}
    }
}

@Composable
fun CommentSection(){
}