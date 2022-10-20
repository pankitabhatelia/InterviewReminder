package activitiy

import adapter.ImageAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.ActivityDaggerHiltDemoBinding
import com.example.interviewreminderapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import viewmodel.HiltDemoViewModel
import viewmodel.LoginViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DaggerHiltDemoActivity : AppCompatActivity() {
    lateinit var recyclerViewAdapter: ImageAdapter
    lateinit var viewModel: HiltDemoViewModel
    lateinit var binding: ActivityDaggerHiltDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaggerHiltDemoBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel=ViewModelProvider(this).get(HiltDemoViewModel::class.java)
        binding.data=viewModel
        setContentView(binding.root)
        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = ImageAdapter()
        findViewById<RecyclerView>(R.id.recyclerview).adapter=recyclerViewAdapter
    }

    private fun initViewModel() {
        viewModel.getLiveDataObserver().observe(this, Observer {
            if(it != null) {
                recyclerViewAdapter.setlistData(it)
                recyclerViewAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "error in getting data", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.loadListOfData()
    }
}