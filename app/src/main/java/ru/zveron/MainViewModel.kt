package ru.zveron

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grpc.owner.OwnerInfoRequest
import com.grpc.owner.OwnerInfoServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _messageResult = MutableStateFlow("")
    val messageResult = _messageResult.asStateFlow()

    fun onSendClick() {
        viewModelScope.launch {
            val r = sendMessage(
                "Message",
                "10.0.2.2",
                "6566"
            )
            if(r is String){
                _messageResult.value = r
            } else if (r is Exception) {
                _messageResult.value = r.message.toString()
            }
        }
    }
}


@Suppress("RedundantSuspendModifier")
suspend fun sendMessage(
    message: String,
    host: String,
    portString: String
): Any? {
    val port = if (TextUtils.isEmpty(portString)) 0 else Integer.valueOf(portString)
    return try {
        val channel: ManagedChannel? = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        val stub = OwnerInfoServiceGrpc.newBlockingStub(channel)
        val request = OwnerInfoRequest.newBuilder().setName(message).build()
        val reply = stub.getName(request)
        reply.message
    } catch (e: Exception) {
        e
    }
}