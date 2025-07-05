package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.data.remote.model.Status
import com.devapp.cookfriends.data.remote.repository.CommentRepository
import com.devapp.cookfriends.data.remote.repository.RecipeRepository
import javax.inject.Inject

class UploadPendingDataUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke() {
        val pendingRecipes = recipeRepository.getPendingUploadRecipes()
        for (recipe in pendingRecipes) {
            try {
                val apiResponse = recipeRepository.upsertRecipesToApi(recipe)
                if (apiResponse.status == Status.SUCCESS.value) {
                    recipeRepository.setUpdated(recipe.id)
                }
            } catch (_: Exception) { }
        }

        val pendingComments = commentRepository.getPendingUploadComments()
        for (comment in pendingComments) {
            try {
                val apiResponse = commentRepository.addCommentApi(comment)
                if (apiResponse.status == Status.SUCCESS.value) {
                    commentRepository.setUpdated(comment.id)
                }
            } catch (_: Exception) { }
        }
    }
}
