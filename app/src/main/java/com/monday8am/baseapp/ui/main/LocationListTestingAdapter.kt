package com.monday8am.baseapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monday8am.baseapp.R
import com.monday8am.baseapp.domain.model.Coordinates
import com.monday8am.baseapp.domain.model.UserLocation
import java.text.DateFormat
import java.util.Date

class LocationListTestingAdapter : ListAdapter<UserLocation, LocationListTestingAdapter.ViewHolder>(LocationItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.test_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position) ?: return
        val date = DateFormat.getDateTimeInstance().format(Date(model.takenAt))
        holder.titleTextView.text = "Distance: ${distanceWithPrevious(position)}m Loc:${model.latitude}/${model.longitude}"
        holder.subtitleTextView.text = "Taken at: $date"
        if (model.imageUrl != null) {
            Glide
                .with(holder.itemView)
                .load(model.imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.iconImageView)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val subtitleTextView: TextView = itemView.findViewById(R.id.subtitle)
        val iconImageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    private fun distanceWithPrevious(position: Int): Int {
        if (position == 0) {
            return 0
        }

        val modelA = getItem(position)
        val modelB = getItem(position - 1)

        val locationA = Coordinates(modelA.longitude, modelA.latitude)
        val locationB = Coordinates(modelB.longitude, modelB.latitude)

        return locationA.distanceTo(locationB)
    }
}
