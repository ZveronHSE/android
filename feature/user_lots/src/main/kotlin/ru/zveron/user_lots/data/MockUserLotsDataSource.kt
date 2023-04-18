package ru.zveron.user_lots.data

import kotlinx.coroutines.delay
import ru.zveron.user_lots.domain.Lot

class MockUserLotsDataSource: UserLotsDataSource {

    private var requestCount = 0

    private val list = listOf(
        Lot(
            id = 1,
            title = "Собака Корги. 3 года. ОТдам на передержку",
            price = "40 000 ₽",
            imageUrl = "https://www.thesprucepets.com/thmb/aWJdOjvIeBNiXbPr9QIdRe9Dz5k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/chinese-dog-breeds-4797219-hero-2a1e9c5ed2c54d00aef75b05c5db399c.jpg",
            isActive = true,
            views = 100,
            likes = 10_000,
        ),
        Lot(
            id = 2,
            title = "Собака Самоед (помесь). 3 года",
            price = "40 000 ₽",
            imageUrl = "https://www.thesprucepets.com/thmb/aWJdOjvIeBNiXbPr9QIdRe9Dz5k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/chinese-dog-breeds-4797219-hero-2a1e9c5ed2c54d00aef75b05c5db399c.jpg",
            isActive = true,
            views = 100,
            likes = 10_000,
        ),
        Lot(
            id = 3,
            title = "Собака Корги архив. ОТдам на передержку",
            price = "40 000 ₽",
            imageUrl = "https://www.thesprucepets.com/thmb/aWJdOjvIeBNiXbPr9QIdRe9Dz5k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/chinese-dog-breeds-4797219-hero-2a1e9c5ed2c54d00aef75b05c5db399c.jpg",
            isActive = false,
            views = 100,
            likes = 10_000,
        ),
        Lot(
            id = 4,
            title = "Собака Самоед (помесь). 3 года",
            price = "40 000 ₽",
            imageUrl = "https://www.thesprucepets.com/thmb/aWJdOjvIeBNiXbPr9QIdRe9Dz5k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/chinese-dog-breeds-4797219-hero-2a1e9c5ed2c54d00aef75b05c5db399c.jpg",
            isActive = true,
            views = 100,
            likes = 10_000,
        ),
        Lot(
            id = 5,
            title = "Собака Корги НУ ТОЧНО НЕ АРХИВ.",
            price = "40 000 ₽",
            imageUrl = "https://www.thesprucepets.com/thmb/aWJdOjvIeBNiXbPr9QIdRe9Dz5k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/chinese-dog-breeds-4797219-hero-2a1e9c5ed2c54d00aef75b05c5db399c.jpg",
            isActive = false,
            views = 100,
            likes = 10_000,
        ),
        Lot(
            id = 6,
            title = "Собака Самоед (помесь). 3 года",
            price = "40 000 ₽",
            imageUrl = "https://www.thesprucepets.com/thmb/aWJdOjvIeBNiXbPr9QIdRe9Dz5k=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/chinese-dog-breeds-4797219-hero-2a1e9c5ed2c54d00aef75b05c5db399c.jpg",
            isActive = true,
            views = 100,
            likes = 10_000,
        ),
    )
    override suspend fun loadLots(): List<Lot> {
        requestCount += 1
        delay(500L)
        if (requestCount <= 2) {
            throw IllegalArgumentException("not enough retries")
        }
        return list
    }
}