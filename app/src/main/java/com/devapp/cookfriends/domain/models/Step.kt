package com.devapp.cookfriends.domain.models

import com.devapp.cookfriends.data.remote.model.StepModel

data class Step(
    var order: Int? = null,
    var content: String? = null,
    var stepPhotos: List<String>? = null
)

fun StepModel.toDomain() = Step(order, content)
