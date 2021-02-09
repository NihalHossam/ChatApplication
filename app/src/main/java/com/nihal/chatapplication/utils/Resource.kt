package com.nihal.chatapplication.utils

/**
 * Represents a network bound resource. Each function represents the resource's state:
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message:String?
){
    companion object{

        /**
         * @param data The data to post.
         * @return The resource being successfully retrieved.
         */
        fun <T> success(data: T?): Resource<T>{
            return Resource(Status.SUCCESS, data, null)
        }

        /**
         * @param msg The error message to be displayed.
         * @param data passed as null.
         * @return The resource retrieving being failed.
         */
        fun <T> error(msg: String, data: T?): Resource<T>{
            return Resource(Status.ERROR, data, msg)
        }

        /**
         * @param data passed as null.
         * @return The resource being retrieved.
         */
        fun <T> loading(data: T?): Resource<T>{
            return Resource(Status.LOADING, data, null)
        }
    }
}