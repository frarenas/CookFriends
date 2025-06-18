package com.devapp.cookfriends.presentation.navigation

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
object Login

@Serializable
object Home

@Serializable
object Recipe

@Serializable
object RecoveryPassword

@Serializable
data class  EditRecipe (val id: Uuid? = null)
