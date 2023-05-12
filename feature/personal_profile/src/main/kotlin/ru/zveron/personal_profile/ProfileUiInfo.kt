package ru.zveron.personal_profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileUiInfo(
    val avatarUrl: String?,
    val name: String,
    val surname: String,
    val addressUiInfo: AddressUiInfo,
): Parcelable

@Parcelize
data class AddressUiInfo(
    val region: String,
    val town: String,
    val longitude: Double,
    val latitude: Double,
): Parcelable
