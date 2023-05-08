package ru.zveron.personal_profile.profile_preview.data

internal data class ProfileInfo(
    val id: Long,
    val name: String,
    val surname: String,
    val avatarUrl: String,
    val rating: Double,
    // TODO: add address
)
