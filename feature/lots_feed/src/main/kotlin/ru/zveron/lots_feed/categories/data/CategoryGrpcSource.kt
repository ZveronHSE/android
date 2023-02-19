package ru.zveron.lots_feed.categories.data

import com.google.protobuf.empty
import com.google.protobuf.int32Value
import com.google.protobuf.util.JsonFormat
import ru.zveron.contract.apigateway.ApigatewayServiceGrpc.ApigatewayServiceBlockingStub
import ru.zveron.contract.apigateway.apiGatewayRequest
import ru.zveron.contract.parameter.external.CategoryResponse
import ru.zveron.lots_feed.mappings.toDomainCategory
import ru.zveron.lots_feed.models.categories.Category

private const val GET_ROOT_METHOD_NAME = "getRoot"
private const val GET_CHILD_METHOD_NAME = "categoryGetChild"

class CategoryGrpcSource(
    private val apigateway: ApigatewayServiceBlockingStub,
): CategorySource {
    override suspend fun loadRootCategories(): List<Category> {
        val getRootRequest = empty {  }
        val request = apiGatewayRequest {
            this.requestBody = getRootRequest.toByteString()
            this.methodAlias = GET_ROOT_METHOD_NAME
        }

        val response = apigateway.callApiGateway(request)

        val responseBuilder = CategoryResponse.newBuilder()
        JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

        return responseBuilder
            .build()
            .categoriesList
            .map { it.toDomainCategory() }
    }

    override suspend fun loadChildCategories(categoryId: Int): List<Category> {
        val getChildrenRequest = int32Value { this.value = categoryId }
        val request = apiGatewayRequest {
            this.requestBody = getChildrenRequest.toByteString()
            this.methodAlias = GET_CHILD_METHOD_NAME
        }

        val response = apigateway.callApiGateway(request)

        val responseBuilder = CategoryResponse.newBuilder()
        JsonFormat.parser().merge(response.responseBody.toStringUtf8(), responseBuilder)

        return responseBuilder
            .build()
            .categoriesList
            .map { it.toDomainCategory() }
    }

}