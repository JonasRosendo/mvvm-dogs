package com.jonasrosendo.mvvmdogs.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.jonasrosendo.mvvmdogs.R
import com.jonasrosendo.mvvmdogs.viewmodel.DogsViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: DogsViewModel
    private val dogsListAdapter = AdapterDogs(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DogsViewModel::class.java) //creates ViewModel instance
        viewModel.refresh() //calls refresh method to populate the list
        //recycler view
        rvDogs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }
        //used to call the refresh method again by swiping from top to bottom
        refreshLayout.setOnRefreshListener {
            //show and hide views
            rvDogs.visibility = View.GONE
            tvError.visibility = View.GONE
            pbLoadingDogsList.visibility = View.VISIBLE
            //refresh view
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }
        //method to observe if there are changes on viewmodel data
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(this, Observer { dogs ->
            dogs?.let {
                rvDogs.visibility = View.VISIBLE
                dogsListAdapter.updateList(it)
            }
        })

        viewModel.hasError.observe(this, Observer { isError ->
            isError?.let {
                tvError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            isLoading?.let {
                pbLoadingDogsList.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.actionSettings -> view?.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToSettingsFragment()) }
        }

        return super.onOptionsItemSelected(item)
    }
}