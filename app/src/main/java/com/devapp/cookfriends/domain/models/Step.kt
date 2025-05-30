package com.devapp.cookfriends.domain.models

import com.devapp.cookfriends.data.model.StepModel

data class Step(
    var order: Int? = null,
    var content: String? = null
)

fun StepModel.toDomain() = Step(order, content)
