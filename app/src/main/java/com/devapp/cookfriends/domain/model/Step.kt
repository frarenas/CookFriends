package com.devapp.cookfriends.domain.model

import com.devapp.cookfriends.data.remote.model.StepModel

data class Step(
    var order: Int? = null,
    var content: String? = null
)

fun StepModel.toDomain() = Step(order, content)
