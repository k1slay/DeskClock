package com.k2.deskclock.location.models

data class LocationError(
    val message: String,
    val code: ErrorType,
)

sealed class ErrorType {
    object LocationDisabled : ErrorType()

    object Unknown : ErrorType()

    object NoPermission : ErrorType()
}
