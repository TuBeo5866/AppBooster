package com.tony.appbooster.domain.model.common

/**
 * A sealed class representing the state of a data operation.
 * It can be either Success, holding the data, or Error, holding a ResourceError.
 *
 * @param T The type of the data in case of success.
 */
sealed class Resource<out T> {
    /**
     * Represents a successful operation.
     * @param data The data retrieved or processed.
     */
    data class Success<T>(val data: T): Resource<T>()

    /**
     * Represents a failed operation.
     * @param data The details of the error, encapsulated in a ResourceError object.
     */
    data class Error(val data: ResourceError): Resource<Nothing>()
}

/**
 * A sealed class representing different types of errors that can occur during an operation.
 */
sealed class ResourceError {
    /**
     * Represents an error in the application logic or data processing.
     * @param errorCode An optional code identifying the specific logic error.
     * @param errorMessage An optional descriptive message for the error.
     * @param errorLevel An optional integer indicating the severity or level of the error (default is 0).
     */
    data class LogicError(
        val errorMessage: String?,
        val errorCode: String? = null,
        val errorLevel: Int = 0
    ): ResourceError()

    /**
     * Represents an error related to network operations.
     * @param errorCode An optional code identifying the specific network error.
     * @param errorMessage An optional descriptive message for the error.
     * @param errorLevel An optional integer indicating the severity or level of the error (default is 0).
     * @param exception An optional Throwable that caused the network error.
     */
    data class NetworkError(
        val errorMessage: String?,
        val errorCode: String? = null,
        val errorLevel: Int = 0,
        val exception: Throwable? = null
    ) : ResourceError()

    /**
     * Represents an error that occurs during a database operation.
     * @param message A descriptive message for the error.
     */
    data class DatabaseError(val message: String) : ResourceError()

    /**
     * Represents an SSL (Secure Sockets Layer) related error.
     */
    data object SSLError: ResourceError()

    /**
     * Represents an unknown or unspecified error.
     */
    data object UnknownError: ResourceError()
}