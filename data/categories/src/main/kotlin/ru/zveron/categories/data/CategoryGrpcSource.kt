package ru.zveron.categories.data

import com.google.protobuf.Empty
import com.google.protobuf.Int32Value
import com.google.protobuf.empty
import com.google.protobuf.int32Value
import ru.zveron.categories.mappings.toDomainCategory
import ru.zveron.categories.models.Category
import ru.zveron.contract.parameter.external.CategoryResponse
import ru.zveron.network.ApigatewayDelegate

private const val GET_ROOT_METHOD_NAME = "categoryRootGet"
private const val GET_CHILD_METHOD_NAME = "categoryChildrenGet"

class CategoryGrpcSource(
    private val apigateway: ApigatewayDelegate,
): CategorySource {
    override suspend fun loadRootCategories(): List<Category> {
        val getRootRequest = empty {  }

        val response = apigateway.callApiGateway<Empty, CategoryResponse>(
            GET_ROOT_METHOD_NAME,
            getRootRequest,
            CategoryResponse.newBuilder(),
        )

        return response.categoriesList.map { it.toDomainCategory() }
    }

    override suspend fun loadChildCategories(categoryId: Int): List<Category> {
        val getChildrenRequest = int32Value { this.value = categoryId }

        val response = apigateway.callApiGateway<Int32Value, CategoryResponse>(
            GET_CHILD_METHOD_NAME,
            getChildrenRequest,
            CategoryResponse.newBuilder(),
        )
        return response.categoriesList.map { it.toDomainCategory() }
    }

}