package com.devapp.cookfriends.presentation.navigation

import kotlinx.serialization.Serializable

interface Screen

@Serializable
object Login: Screen

@Serializable
object Home: Screen

@Serializable
object Recipe: Screen

@Serializable
object RecoveryPassword: Screen