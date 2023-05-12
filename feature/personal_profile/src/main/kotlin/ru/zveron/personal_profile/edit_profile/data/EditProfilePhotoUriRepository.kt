package ru.zveron.personal_profile.edit_profile.data

internal class EditProfilePhotoUriRepository {
    private var currentUri: String? = null

    fun saveUri(uri: String) {
        currentUri = uri
    }

    fun getCurrentUri(): String? {
        return currentUri
    }
}