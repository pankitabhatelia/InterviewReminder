package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewreminderapp.databinding.UpcomingItemBinding
import model.AddInterviewModel

class UpcomingAdapter : RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {
    private var interviewList = ArrayList<AddInterviewModel>()
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(data: AddInterviewModel)
    }
    fun setOnItemClickListener(listener:OnItemClickListener) {
        itemClickListener = listener
    }

    fun setData(interView: ArrayList<AddInterviewModel>) {
        this.interviewList = interView
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UpcomingItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = interviewList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return interviewList.size
    }

    inner class ViewHolder(val binding: UpcomingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AddInterviewModel) {
            binding.addData = data
            binding.executePendingBindings()
        }

    }
}