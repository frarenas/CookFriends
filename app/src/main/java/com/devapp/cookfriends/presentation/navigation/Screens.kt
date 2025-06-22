package com.devapp.cookfriends.presentation.navigation

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

interface Screen

@Serializable
object Login: Screen

@Serializable
object Home: Screen

@Serializable
object Recipe: Screen

@Serializable
object RecoveryPassword: Screen

@Serializable
data class  EditRecipe (val id: Uuid? = null): Screen
