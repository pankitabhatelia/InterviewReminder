package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewreminderapp.databinding.DoneItemBinding
import model.AddInterviewModel

class DoneAdapter: RecyclerView.Adapter<DoneAdapter.ViewHolder>() {
    private var interviewList = ArrayList<AddInterviewModel>()

    fun setData(interView: ArrayList<AddInterviewModel>) {
        this.interviewList = interView
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DoneItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = interviewList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return interviewList.size
    }

    inner class ViewHolder(val binding: DoneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AddInterviewModel) {
            binding.doneData = data
            binding.executePendingBindings()
        }

    }
}