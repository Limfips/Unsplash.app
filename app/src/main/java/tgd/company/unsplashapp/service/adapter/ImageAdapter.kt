package tgd.company.unsplashapp.service.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.data.photo.Photo
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide: RequestManager
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var images: List<Photo>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_photo,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Photo) -> Unit)? = null
    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.itemView.apply {
            glide.load(image.urls.small).into(findViewById(R.id.ivItemPhoto))

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(image)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}