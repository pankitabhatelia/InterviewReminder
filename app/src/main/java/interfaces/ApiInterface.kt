package interfaces

import model.RecyclerList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("repositories")
    fun getDataFromAPI(@Query("q") query: String): Call<RecyclerList>
}