package activitiy

import adapter.ImageAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.ActivityDaggerHiltDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import viewmodel.HiltDemoViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DaggerHiltDemoActivity : AppCompatActivity() {
    private lateinit var adapter: ImageAdapter
    private lateinit var binding:ActivityDaggerHiltDemoBinding
    lateinit var viewModel: HiltDemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaggerHiltDemoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HiltDemoViewModel::class.java]
        binding.data = viewModel
        setContentView(binding.root)
        initRecyclerview()
        observer()

    }

    private fun observer() {
       viewModel.imageList.observe(this, Observer {
           if(it!=null){
               it.hits?.let { it1 -> adapter.setData(it1) }
           }
           viewModel.loadImages()
       })
    }

    private fun initRecyclerview(){
        adapter = ImageAdapter()
        binding.rv.adapter = adapter

    }
}