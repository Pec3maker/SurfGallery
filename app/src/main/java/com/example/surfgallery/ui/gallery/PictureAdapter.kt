package com.example.surfgallery.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.surfgallery.R
import com.example.surfgallery.databinding.PictureItemBinding
import com.example.surfgallery.domain.models.Picture

class PictureAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<PictureAdapter.PictureHolder>() {

    private var pictureList = emptyList<Picture>()

    inner class PictureHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = PictureItemBinding.bind(item)

        fun bind(picture: Picture) = with(binding) {
            ivPicture.load(data = picture.photoUrl)
            tvPicture.text = picture.title

            binding.ivPicture.setOnClickListener {
                onItemClick(picture.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_item, parent, false)
        return PictureHolder(item = view)
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.bind(picture = pictureList[position])
    }

    override fun getItemCount(): Int = pictureList.size

    fun addAllPictures(pics: List<Picture>) {
        pictureList = pics
        notifyDataSetChanged()
    }
}