package com.bn.meow.domain.usecase

import com.bn.meow.domain.repository.MainRepository


class GetCatsUseCase
constructor(
    private val repo: MainRepository,
) {
    suspend fun cats(limit:Int) = repo.getCats(limit)
}