package com.devapp.cookfriends.data.remote.repository

import com.devapp.cookfriends.data.local.dao.CommentDao
import com.devapp.cookfriends.data.local.entity.toDatabase
import com.devapp.cookfriends.data.remote.model.toModel
import com.devapp.cookfriends.data.remote.service.CookFriendsService
import com.devapp.cookfriends.domain.model.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.uuid.Uuid

class CommentRepository @Inject constructor(
    private val service: CookFriendsService,
    private val commentDao: CommentDao
) {

    suspend fun addCommentDb(comment: Comment) =
        withContext(Dispatchers.IO) {
            commentDao.insert(comment.toDatabase())
        }

    suspend fun addCommentApi(comment: Comment) =
        withContext(Dispatchers.IO) {
            service.addComment(comment.toModel())
        }

    suspend fun setUpdated(id: Uuid) {
        return withContext(Dispatchers.IO) {
            commentDao.setUpdated(id)
        }
    }
}
