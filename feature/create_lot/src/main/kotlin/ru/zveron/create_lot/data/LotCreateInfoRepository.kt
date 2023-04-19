package ru.zveron.create_lot.data

class LotCreateInfoRepository {
    private val photoUrls = mutableListOf<String>()
    private var lotName: String = ""

    fun setName(name: String) {
        lotName = name
    }

    fun addPhotoUrl(url: String) {
        photoUrls.add(url)
    }
}