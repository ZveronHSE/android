package ru.zveron.create_lot.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class LotCreatePhotoStateRepository {
    private val _photoStates = MutableStateFlow<List<PhotoState>>(emptyList())
    val photoStates = _photoStates.asStateFlow()

    fun updateStateForId(id: String, status: PhotoUploadStatus) {
        _photoStates.update { states ->
            states.toMutableList().map {
                if (it.id != id) {
                    it
                } else {
                    it.copy(status = status)
                }
            }
        }
    }

    fun addNewPhoto(url: String): String {
        val newId = UUID.randomUUID().toString()
        _photoStates.update { states ->
            states.toMutableList().apply {
                add(PhotoState(newId, url, PhotoUploadStatus.LOADING))
            }
        }
        return newId
    }
}

enum class PhotoUploadStatus {
    SUCCESS,
    LOADING,
    ERROR,
}

data class PhotoState(val id: String, val url: String, val status: PhotoUploadStatus)
