package com.devapp.cookfriends.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.devapp.cookfriends.domain.model.StepPhoto
import kotlin.uuid.Uuid

@Entity(
    tableName = "step_photo_table",
    foreignKeys = [ForeignKey(
            entity = StepEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("step_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class StepPhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Uuid,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "step_id", index = true) val stepId: Uuid
)

fun StepPhoto.toDatabase() = StepPhotoEntity(
    id = id,
    url = url,
    stepId = stepId,
)
