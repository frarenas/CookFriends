package com.devapp.cookfriends.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import com.devapp.cookfriends.ui.theme.Red
import com.devapp.cookfriends.ui.theme.White

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val homeState by viewModel.homeState.collectAsState()

    Scaffold(
            modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar( windowInsets = NavigationBarDefaults.windowInsets ){

                NavigationBarItem(
                    selected = true,
                    onClick = { /*TODO*/ },
                    icon = { Icon( Icons.Outlined.Home, null) },
                    label = { Text(text = "Recetas")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon( Icons.Outlined.Favorite, null) },
                    label = { Text(text = "Favoritos")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon( Icons.Outlined.Favorite, null) },
                    label = { Text(text = "Mis recetas")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon( Icons.Outlined.Person, null) },
                    label = { Text(text = "Perfil")}
                )
            }
        }
        ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Header(
                modifier = Modifier
            )
            LazyColumn {
                items(homeState.recipeList) { item ->
                    Row(
                        modifier = Modifier.padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://www.clarin.com/2024/03/12/1jgNm90_r_340x340__1.jpg",
                            contentDescription = null,
                            modifier = Modifier.width(100.dp).height(100.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = item.name!!, fontSize = 24.sp)
                            Text(text = "Categoría: ${item.type}")
                            Text(text = "Autor: ${item.author}")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorito", tint = Red)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        White,
                        MaterialTheme.colorScheme.primary
                    )
                )
            )
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = modifier.background(White).fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            label = { Text("Buscar recetas...") },
            shape = RoundedCornerShape(20.dp)
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Recetas",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CookFriendsTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    CookFriendsTheme {
        Header(Modifier)
    }
}