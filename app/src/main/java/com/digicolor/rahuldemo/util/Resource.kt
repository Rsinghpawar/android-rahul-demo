package com.digicolor.rahuldemo.util

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data : T) : Resource<T>()
    data class Error(val message: String, val type: ErrorType = ErrorType.UNKNOWN) : Resource<Nothing>()
}

enum class ErrorType {
    NO_INTERNET,
    SERVER_ERROR,
    UNKNOWN
}