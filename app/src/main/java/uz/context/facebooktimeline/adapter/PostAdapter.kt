package uz.context.facebooktimeline.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.context.facebooktimeline.R
import uz.context.facebooktimeline.model.Post
import uz.context.facebooktimeline.util.OnClickListener

class PostAdapter(
    private val context: Context,
    private val items: ArrayList<Post>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(post: Post) {
        items.add(post)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is PostViewHolder) {
            holder.apply {
                tvTitle.text = item.title
                tvDes.text = item.description
            }

            Glide.with(context)
                .load(item.imageView)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_link2)
        val tvTitle: TextView = view.findViewById(R.id.text_title2)
        val tvDes: TextView = view.findViewById(R.id.text_des2)
    }
}