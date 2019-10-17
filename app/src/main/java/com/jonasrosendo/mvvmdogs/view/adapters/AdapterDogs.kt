package com.jonasrosendo.mvvmdogs.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jonasrosendo.mvvmdogs.R
import com.jonasrosendo.mvvmdogs.databinding.AdapterDogsBinding
import com.jonasrosendo.mvvmdogs.model.model.Dog
import com.jonasrosendo.mvvmdogs.view.fragments.ListFragmentDirections
import com.jonasrosendo.mvvmdogs.view.listeners.DogClickListener
import kotlinx.android.synthetic.main.adapter_dogs.view.*

class AdapterDogs(private val dogList:ArrayList<Dog>) :
    RecyclerView.Adapter<AdapterDogs.ViewHolder>(),
    DogClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<AdapterDogsBinding>(inflater, R.layout.adapter_dogs, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dogList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.containerView.dog = dogList[position]
        holder.containerView.listener = this
    }

    class ViewHolder(val containerView: AdapterDogsBinding) : RecyclerView.ViewHolder(containerView.root){
        //empty
    }

    fun updateList(newDogList: List<Dog>){
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onDogClick(v: View) {
        val dogUuid = v.dogId.text.toString().toInt()
        val action =
            ListFragmentDirections.actionListFragmentToDetailFragment()
        action.dogUuid = dogUuid
        Navigation.findNavController(v).navigate(action)
    }
}