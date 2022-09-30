package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewreminderapp.databinding.CancelledItemBinding
import model.AddInterviewModel

class CancelAdapter : RecyclerView.Adapter<CancelAdapter.ViewHolder>() {
    private var interviewList = ArrayList<AddInterviewModel>()
    private var itemClickListener: ((AddInterviewModel) -> Unit)? = null

    fun setOnItemClickListener(data :(AddInterviewModel)->Unit) {
        itemClickListener = data
    }

    fun setData(interView: ArrayList<AddInterviewModel>) {
        this.interviewList = interView
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancelAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CancelledItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CancelAdapter.ViewHolder, position: Int) {
        val currentItem = interviewList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return interviewList.size
    }

    inner class ViewHolder(val binding: CancelledItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AddInterviewModel) {
            binding.cancelledData = data
            binding.executePendingBindings()
        }

    }
}