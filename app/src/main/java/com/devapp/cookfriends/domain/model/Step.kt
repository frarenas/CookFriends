package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.remote.model.StepModel
import kotlin.uuid.Uuid

data class Step(
    var id: Uuid = Uuid.random(),
    var order: Int? = null,
    var content: String? = null,
    var recipeId: Uuid
)

fun StepModel.toDomain() = Step(id, order, content, recipeId)
