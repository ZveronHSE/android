package ru.zveron.image_storage

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.zveron.image_storage.data.convert.ImageConvertRepository
import ru.zveron.image_storage.data.convert.ImageConvertRepositoryImpl
import ru.zveron.image_storage.data.upload.ImageUploadRepository
import ru.zveron.image_storage.data.upload.ImageUploadRepositoryImpl
import ru.zveron.image_storage.domain.UploadImageInteractor
import ru.zveron.image_storage.domain.UploadImageInteractorImpl

val imageStorageModule = module {
    singleOf(::ImageUploadRepositoryImpl) bind ImageUploadRepository::class
    singleOf(::ImageConvertRepositoryImpl) bind ImageConvertRepository::class
    singleOf(::UploadImageInteractorImpl) bind UploadImageInteractor::class
}