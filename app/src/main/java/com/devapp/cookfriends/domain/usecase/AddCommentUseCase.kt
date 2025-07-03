package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.model.Status
import com.devapp.cookfriends.data.remote.repository.CommentRepository
import com.devapp.cookfriends.domain.model.Comment
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val repository: CommentRepository) {
    suspend operator fun invoke(comment: Comment) {
        repository.addCommentDb(comment)
        val apiResponse = repository.addCommentApi(comment)
        if (apiResponse.status == Status.SUCCESS.value) {
            repository.setUpdated(comment.id)
        }
    }
}
