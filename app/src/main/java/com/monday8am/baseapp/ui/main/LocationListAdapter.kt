package com.monday8am.baseapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monday8am.baseapp.R
import com.monday8am.baseapp.domain.model.UserLocation

class LocationListAdapter : ListAdapter<UserLocation, LocationListAdapter.ViewHolder>(LocationItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position) ?: return
        when (model.imageUrl) {
            null -> holder.spinner.visibility = View.VISIBLE
            else -> {
                Glide
                    .with(holder.itemView)
                    .load(model.imageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewBig)
        val spinner: ProgressBar = itemView.findViewById(R.id.progressBar)
    }
}

object LocationItemDiff : DiffUtil.ItemCallback<UserLocation>() {
    override fun areItemsTheSame(oldItem: UserLocation, newItem: UserLocation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserLocation, newItem: UserLocation): Boolean {
        return oldItem == newItem
    }
}
