package com.udacity.asteroidradar.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R


@BindingAdapter("imgUrl")
fun loadImageFromUrl(imageView: ImageView, imgUrl: String?) {
        Picasso.with(imageView.context)
            .load(imgUrl?.toUri())
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.ic_broken_image)
            .into(imageView)
    }

    @BindingAdapter("statusIcon")
    fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
        if (isHazardous) {
            imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        } else {
            imageView.setImageResource(R.drawable.ic_status_normal)
        }
    }

    @BindingAdapter("asteroidStatusImage")
    fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
        if (isHazardous) {
            imageView.setImageResource(R.drawable.asteroid_hazardous)
        } else {
            imageView.setImageResource(R.drawable.asteroid_safe)
        }
    }

    @BindingAdapter("astronomicalUnitText")
    fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
        val context = textView.context
        textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    }

    @BindingAdapter("kmUnitText")
    fun bindTextViewToKmUnit(textView: TextView, number: Double) {
        val context = textView.context
        textView.text = String.format(context.getString(R.string.km_unit_format), number)
    }

    @BindingAdapter("velocityText")
    fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
        val context = textView.context
        textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
    }