package ru.zveron.models.mappings

import ru.zveron.models.gender.Gender
import ru.zveron.contract.lot.model.Gender as GrpcGender

fun Gender.toGrpcGender(): GrpcGender {
    return when (this) {
        Gender.MALE -> GrpcGender.MALE
        Gender.FEMALE -> GrpcGender.FEMALE
        Gender.METIS -> GrpcGender.METIS
        Gender.UNKNOWN -> GrpcGender.UNRECOGNIZED
    }
}