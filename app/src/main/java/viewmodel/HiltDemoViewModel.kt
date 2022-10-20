package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import model.PixabayResponse
import model.RecyclerData
import repository.RetroRepository
import javax.inject.Inject

@HiltViewModel
class HiltDemoViewModel @Inject constructor(private val repository: RetroRepository) :
    ViewModel() {

    lateinit var liveDataList: MutableLiveData<List<RecyclerData>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<RecyclerData>> {
        return liveDataList
    }

    fun loadListOfData() {
        repository.makeApiCall("ny", liveDataList)
    }
}


