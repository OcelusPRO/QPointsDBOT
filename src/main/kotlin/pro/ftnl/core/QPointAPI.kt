package pro.ftnl.core

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.joda.time.DateTime
import pro.ftnl.CONFIG


class QPointAPI {

    val baseUrl: String = CONFIG.apiURL + "/api"

    private fun modifyQpoints(url: String, data: List<ModifyUserQpoints>){
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType = "application/json".toMediaType()
        val body: RequestBody = Gson().toJson(data).toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url(url)
            .method("PATCH", body)
            .addHeader("Authorization", "Basic ${CONFIG.apiKey}")
            .addHeader("Content-Type", "application/json")
            .build()
        val response: Response = client.newCall(request).execute()
    }

    fun addQpoints(qpoints: Int, userIds: List<String>) {
        val url = "$baseUrl/qpoints/discord/add"
        modifyQpoints(url, userIds.map { ModifyUserQpoints(it, null, qpoints) })
    }
    fun removeQpoints(qpoints: Int, userIds: List<String>) {
        val url = "$baseUrl/qpoints/discord/remove"
        modifyQpoints(url, userIds.map { ModifyUserQpoints(it, null, qpoints) })
    }
    fun setQpoints(qpoints: Int, userIds: List<String>) {
        val url = "$baseUrl/qpoints/discord/set"
        modifyQpoints(url, userIds.map { ModifyUserQpoints(it, null, qpoints) })
    }

    fun getMemberData(userId: String): User {
        val url = "$baseUrl/qpoints/discord/$userId"
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Authorization", "Basic ${CONFIG.apiKey}")
            .build()
        val response: Response = client.newCall(request).execute()
        return Gson().fromJson(response.body?.string(), User::class.java)
    }


    data class ModifyUserQpoints(val discordId: String, val twitchId: String? = null, val toTransfer: Int)
    data class User(
        val id: Int,
        val twitchId: String? = null,
        var discordId: String? = null,
        var qpoints: Int,
        val transactions: List<QpointsTransaction>? = null
    )
    data class QpointsTransaction(
        val id: Int,
        val from: User,
        val to: User,
        val amount: Int,
        val createdAt: DateTime,
        val reason: String? = null,
    )
}