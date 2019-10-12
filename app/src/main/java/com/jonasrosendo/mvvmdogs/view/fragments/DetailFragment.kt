package com.jonasrosendo.mvvmdogs.view.fragments


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.jonasrosendo.mvvmdogs.R
import com.jonasrosendo.mvvmdogs.databinding.FragmentDetailBinding
import com.jonasrosendo.mvvmdogs.model.model.DogPalette
import com.jonasrosendo.mvvmdogs.view.DetailFragmentArgs
import com.jonasrosendo.mvvmdogs.viewmodel.DogDetailsViewModel

class DetailFragment : Fragment() {

    private var dogUuid: Int = 0
    private lateinit var viewModel: DogDetailsViewModel

    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }
        viewModel = ViewModelProviders.of(this).get(DogDetailsViewModel::class.java)
        viewModel.fetch(dogUuid)

        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.dogLiveData.observe(this, Observer { dog ->
            dog?.let { it ->
                dataBinding.dog = it
                it.imageUrl?.let { url ->
                    setupBackgroundColor(url)
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate {palette ->
                        val color: Int = palette?.darkVibrantSwatch?.rgb ?: 0
                        val myPalette = DogPalette(color)
                        dataBinding.palette = myPalette
                    }
                }
            })
    }
}
