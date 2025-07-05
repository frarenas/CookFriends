package com.devapp.cookfriends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.devapp.cookfriends.data.local.entity.CommentEntity
import com.devapp.cookfriends.data.local.entity.CommentWithUser
import kotlin.uuid.Uuid

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    suspend fun insert(comment: CommentWithUser) {
        insertComment(comment.comment)
    }

    @Query("UPDATE comment_table SET update_pending = 0 WHERE id = :id")
    fun setUpdated(id: Uuid)

    @Query("UPDATE comment_table SET update_pending = 0 WHERE id IN(:ids)")
    fun setUpdated(ids: List<Uuid>)

    @Transaction
    @Query("SELECT * FROM comment_table r WHERE update_pending = 1")
    fun getPendingUploadComments(): List<CommentWithUser>
}
