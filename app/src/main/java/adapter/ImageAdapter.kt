package adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interviewreminderapp.databinding.ActivityImageItemBinding
import model.Hit

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var hit= ArrayList<Hit>()

    fun setData(data : List<Hit>) {
        hit.clear()
        this.hit.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding= ActivityImageItemBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hit[position])
        Glide.with(holder.itemView.context).load(hit[position].largeImageURL).into(holder.binding.gridImage)
    }

    override fun getItemCount(): Int {
        return hit.size
    }
    class ViewHolder(val binding:ActivityImageItemBinding) : RecyclerView.ViewHolder(binding.root)  {

        fun bind(image:Hit){
            binding.gridItem=image
            binding.executePendingBindings()
        }
    }
}