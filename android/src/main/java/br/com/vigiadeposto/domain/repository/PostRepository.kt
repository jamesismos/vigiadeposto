package br.com.vigiadeposto.domain.repository

import br.com.vigiadeposto.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPosts(): Flow<List<Post>>
    suspend fun getPostById(id: String): Post?
    suspend fun addPost(post: Post)
    suspend fun updatePost(post: Post)
    suspend fun deletePost(id: String)
}
