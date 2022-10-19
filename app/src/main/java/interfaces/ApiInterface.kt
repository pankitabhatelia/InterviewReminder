package interfaces

import model.PixabayResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface ApiInterface {

    @GET("api/?")
    fun getGridImage(@QueryMap parameter:Map<String,String>): Call<PixabayResponse>
}