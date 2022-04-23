package io.github.instakiller.models

data class CommandLineLogin(
    val username: String,
    val password: String,
    val isAutosaving: Boolean
)
