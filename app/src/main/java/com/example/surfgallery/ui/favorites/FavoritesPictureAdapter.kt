package com.example.surfgallery.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.surfgallery.R
import com.example.surfgallery.databinding.PictureItemFavoriteBinding
import com.example.surfgallery.domain.models.Picture
import com.example.surfgallery.extensions.formatTimeStampToDate

class FavoritesPictureAdapter(
    private val onItemClick: (String) -> Unit,
    private val onFavoriteClick: (Picture) -> Unit
) : RecyclerView.Adapter<FavoritesPictureAdapter.PictureHolder>() {

    private var pictureList = emptyList<Picture>()

    inner class PictureHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = PictureItemFavoriteBinding.bind(item)

        fun bind(picture: Picture) = with(binding) {
            ivPictureFavorites.load(data = picture.photoUrl)
            tvTitleFavorites.text = picture.title
            cbPictureFavorites.isChecked = picture.isInDatabase == true
            tvDateFavorites.text = picture.publicationDate.formatTimeStampToDate()
            tvDescriptionFavorites.text = picture.content


            binding.ivPictureFavorites.setOnClickListener {
                onItemClick(picture.id)
            }
            binding.cbPictureFavorites.setOnClickListener {
                onFavoriteClick(picture)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesPictureAdapter.PictureHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.picture_item_favorite, parent, false)
        return PictureHolder(item = view)
    }

    override fun onBindViewHolder(holder: FavoritesPictureAdapter.PictureHolder, position: Int) {
        holder.bind(picture = pictureList[position])
    }

    override fun getItemCount(): Int = pictureList.size

    fun addAllPictures(pics: List<Picture>) {
        pictureList = pics
        notifyDataSetChanged()
    }
}