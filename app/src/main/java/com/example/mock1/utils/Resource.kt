package com.example.mock1.utils

import com.example.mock1.UsersModel
import io.grpc.internal.SharedResourceHolder

sealed class Resource<out T> : SharedResourceHolder.Resource<UsersModel> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()

    override fun create(): UsersModel {
        TODO("Not yet implemented")
    }

    override fun close(instance: UsersModel?) {
        TODO("Not yet implemented")
    }
}