package repository

import androidx.lifecycle.MutableLiveData
import interfaces.ApiInterface
import model.PixabayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RetroRepository @Inject constructor(private val retroInstance: ApiInterface) {

    fun makeApiCall(parameter:Map<String,String>,liveData: MutableLiveData<PixabayResponse>){
        val call: Call<PixabayResponse> = retroInstance.getGridImage(parameter)
        call.enqueue(object : Callback<PixabayResponse> {
            override fun onResponse(
                call: Call<PixabayResponse>,
                response: Response<PixabayResponse>
            ) {
                liveData.postValue(response.body())
            }

            override fun onFailure(call: Call<PixabayResponse>, t: Throwable) {
                liveData.postValue(null)
            }

        })
    }
}