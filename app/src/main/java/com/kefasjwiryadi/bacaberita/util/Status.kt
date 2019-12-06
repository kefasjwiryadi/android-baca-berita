package com.kefasjwiryadi.bacaberita.util

class Status {

    companion object {
        const val IDLE = 0
        const val SUCCESS = 1
        const val FAILURE = 2
        const val LOADING = 3
        const val LOADING_MORE = 4
        const val NO_RESULT = 5
    }

}

fun Int.isNotLoading() = this != Status.LOADING && this != Status.LOADING_MORE