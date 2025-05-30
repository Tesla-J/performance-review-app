package dev.rmarcos.performancereviewapp.model

enum class Permission {
    COLABORATOR,
    MANAGER,
    ADMIN,
}

data class User (
    val uuid: String,
    val username: String,
    val email: String,
    val password: String,
    val permission: Permission,
    val profile: Profile
)