package com.example.juaraandroid_pokemonapp.core.data.datasource.remote

import com.junemon.pokemon.core.data.dataSource.remote.helper.ApiResult
import com.junemon.pokemon.core.data.dataSource.remote.helper.RetrofitHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Created by Ian Damping on 07,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
class RetrofitHelperImpl @Inject constructor(private val ioDispatcher: CoroutineDispatcher) :
    RetrofitHelper {
    override suspend fun <T> safeApiCalls(call: suspend () -> Response<T>): ApiResult<T> {
        return withContext(ioDispatcher) {
            try {
                val response = call.invoke()
                if (!response.isSuccessful) {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    ApiResult.Error(
                        message = errorMsg,
                        code = response.code()
                    )
                } else {
                    val body = response.body()
                    if (body != null) {
                        ApiResult.Success(body)
                    } else {
                        ApiResult.Error(
                            message = "Response body is empty",
                            code = response.code()
                        )
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> ApiResult.Error("Waktu koneksi habis (Timeout)")
                    is IOException -> ApiResult.Error("Cek koneksi internet kamu")
                    else -> ApiResult.Error("Terjadi kesalahan: ${e.localizedMessage}")
                }
            }
        }
    }
}