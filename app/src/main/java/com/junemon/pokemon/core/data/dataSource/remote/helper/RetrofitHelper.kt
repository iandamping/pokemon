package com.junemon.pokemon.core.data.dataSource.remote.helper

import retrofit2.Response

/**
 * Created by Ian Damping on 07,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface RetrofitHelper {
    suspend fun <T> safeApiCalls(call: suspend () -> Response<T>): ApiResult<T>
}