package tgd.company.unsplashapp.service.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tgd.company.unsplashapp.R
import tgd.company.unsplashapp.data.colection.Collection
import tgd.company.unsplashapp.data.photo.Photo
import javax.inject.Inject

class CollectionsAdapter @Inject constructor(
    private val glide: RequestManager
): RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {
    class CollectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.id == newItem.id
        }
    }

    fun clear() {
        collections = listOf()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var collections: List<Collection>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_collection,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Collection) -> Unit)? = null
    fun setOnItemClickListener(listener: (Collection) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = collections[position]
        holder.itemView.apply {
            glide.load(collection.cover_photo.urls.small).into(findViewById(R.id.ivItemCollection))
            findViewById<TextView>(R.id.tvTitleCollection).text = collection.title
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(collection)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return collections.size
    }
}