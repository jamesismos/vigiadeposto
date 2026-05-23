package br.com.vigiadeposto.domain.usecase

import br.com.vigiadeposto.domain.model.Post
import br.com.vigiadeposto.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> {
        return postRepository.getAllPosts()
    }
}
