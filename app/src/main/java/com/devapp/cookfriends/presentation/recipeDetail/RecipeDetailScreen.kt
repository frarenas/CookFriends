package com.devapp.cookfriends.presentation.recipeDetail

import android.widget.Toolbar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.devapp.cookfriends.domain.models.Ingredient
import com.devapp.cookfriends.domain.models.Recipe
import com.devapp.cookfriends.domain.models.Step

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNREACHABLE_CODE")
@Preview
@Composable
//fun RecipeDetailScreen(viewModel: RecipeDetailViewModel = hiltViewModel()){
fun RecipeDetailScreen(){

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
                title = {Text(exampleRecipe.name)},
                navigationIcon = {
                    IconButton(onClick = TODO()) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
        )}
    ){
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            recipeHeader()
            ingredientsSection()
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
fun ingredientsSection(){

}

@Composable
fun stepsSection(steps: List<Step>){

    steps.forEach(){
        s -> Row {
        AsyncImage(
            model = "https://www.clarin.com/2024/03/12/1jgNm90_r_340x340__1.jpg",
            contentDescription = "foto portada de la receta",
            modifier = Modifier.fillMaxWidth()
                .height(100.dp)
        )
        Column {
            Text("Paso "+s.order)
            Text(s.content.toString())
        }
    }}

}

@Composable
fun CommentSection(){
}