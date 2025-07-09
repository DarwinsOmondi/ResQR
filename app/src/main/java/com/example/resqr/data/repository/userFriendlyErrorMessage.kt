package com.example.resqr.data.repository


fun userFriendlyErrorMessage(message: String?): String {
    return when {
        message == null -> "unknown error occurred"
        message.contains("unauthorized", ignoreCase = true) ->
            "You are not logged in. Please sign in."

        message.contains("forbidden", ignoreCase = true) ->
            "You don't have permission to perform this action."

        message.contains("timeout", ignoreCase = true) ||
                message.contains("unreachable", ignoreCase = true) ->
            "Network issue. Please try again later."

        message.contains("internal Server Error", ignoreCase = true) ->
            "A server error occurred. Try again later."

        else -> "An unexpected error occurred: $message"
    }
}