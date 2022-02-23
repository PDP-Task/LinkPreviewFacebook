package uz.context.facebooktimeline.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.context.facebooktimeline.R
import uz.context.facebooktimeline.model.Story

class StoryAdapter(
    private val items: ArrayList<Story>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            1
        else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.create_story, parent, false)
            return CreateViewHolder(view)
        }
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_view, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is StoryViewHolder) {
            holder.apply {
                ivBackground.setImageResource(item.image)
                ivProfile.setImageResource(item.image)
                tvTitle.text = item.title
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.iv_profile)
        val ivBackground: ImageView = view.findViewById(R.id.iv_background)
        val tvTitle: TextView = view.findViewById(R.id.tv_fullname)
    }

    class CreateViewHolder(view: View) : RecyclerView.ViewHolder(view)
}