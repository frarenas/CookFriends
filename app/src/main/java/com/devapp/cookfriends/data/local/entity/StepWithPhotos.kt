package com.devapp.cookfriends.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.devapp.cookfriends.domain.model.Step

data class StepWithPhotos(
    @Embedded
    val step: StepEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "step_id",
        entity = StepPhotoEntity::class
    )
    val photos: List<StepPhotoEntity>
)

fun Step.toDatabase() = StepWithPhotos(
    step = StepEntity(
        id = id,
        order = order,
        content = content,
        recipeId = recipeId
    ),
    photos = photos.map { photo -> photo.toDatabase() }
)
