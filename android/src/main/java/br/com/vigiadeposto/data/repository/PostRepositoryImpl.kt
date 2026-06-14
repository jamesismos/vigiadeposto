package br.com.vigiadeposto.data.repository

import br.com.vigiadeposto.data.local.PostDao
import br.com.vigiadeposto.data.mapper.toDomain
import br.com.vigiadeposto.data.mapper.toEntity
import br.com.vigiadeposto.domain.model.Post
import br.com.vigiadeposto.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao
) : PostRepository {

    override fun getAllPosts(): Flow<List<Post>> {
        return postDao.getAllPosts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getPostById(id: String): Post? {
        return postDao.getPostById(id)?.toDomain()
    }

    override suspend fun addPost(post: Post) {
        postDao.insertPost(post.toEntity())
    }

    override suspend fun updatePost(post: Post) {
        postDao.updatePost(post.toEntity())
    }

    override suspend fun deletePost(id: String) {
        postDao.deletePost(id)
    }
}
