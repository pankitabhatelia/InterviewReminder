package adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.ActivityImageItemBinding

import model.Hit
import model.RecyclerData

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var listData: List<RecyclerData>? = null

    fun setlistData(listData: List<RecyclerData>?) {
        this.listData = listData
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.bind(listData?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(listData ==null)return 0
        return listData?.size!!
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val thumbImage = view.findViewById<ImageView>(R.id.thumbImage)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvDesc =view.findViewById<TextView>(R.id.tvDesc)

        fun bind(data: RecyclerData) {
            tvName.text = data.name
            tvDesc.text = data.description

            Glide.with(thumbImage)
                .load(data.owner?.avatar_url)
                .into(thumbImage)
        }
    }
}