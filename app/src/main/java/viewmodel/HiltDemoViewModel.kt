package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import model.PixabayResponse
import repository.RetroRepository
import javax.inject.Inject

@HiltViewModel
class HiltDemoViewModel @Inject constructor(private val repository: RetroRepository) :
    ViewModel() {

    var imageList: MutableLiveData<PixabayResponse> = MutableLiveData()

     fun loadImages() {
        val map: HashMap<String, String> = HashMap()
        map["key"] = "26753293-72f2cdb645b61b263b778d4a6"
        map["orientation"] = "horizontal"
        map["order"] = "popular"
        map["page"] = "1"
        map["per_page"] = "20"
        map["safesearch"] = "true"
        map["q"] = "architecture"
        repository.makeApiCall(map, imageList)
    }
}

