package br.com.vigiadeposto.data.mapper

import br.com.vigiadeposto.data.local.PostEntity
import br.com.vigiadeposto.domain.model.Post

fun PostEntity.toDomain(): Post {
    return Post(
        id = id,
        name = name,
        address = address,
        latitude = latitude,
        longitude = longitude,
        isMonitored = isMonitored
    )
}

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        name = name,
        address = address,
        latitude = latitude,
        longitude = longitude,
        isMonitored = isMonitored
    )
}
