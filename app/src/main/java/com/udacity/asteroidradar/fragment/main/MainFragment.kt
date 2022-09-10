package com.udacity.asteroidradar.fragment.main

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.main.Filter
import com.udacity.asteroidradar.main.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

 override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Giving the binding access to the MainViewModel
        binding.viewModel = viewModel
        //TODO pass dumnmy data list into recydlerview adapter, li
        // viewModel.asteroidList.value
        binding.asteroidRecycler.adapter = AsteroidAdapter(
            AsteroidAdapter.OnClickListener{
                viewModel.onAsteroidClicked(it)

            })
        viewModel.navigationToDetail.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onAsteroidNavigated()

            }
        })


     viewModel.imageIOTD.observe(viewLifecycleOwner, Observer { imageUrl ->
            Picasso.with(context).load(imageUrl.toUri()).into(binding.activityMainImageOfTheDay)
        })

     viewModel.asteroidList.observe(viewLifecycleOwner, Observer { asteroidList ->
         (binding.asteroidRecycler.adapter as AsteroidAdapter).submitList(asteroidList)
     })

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

   /* override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.getImage()
        }
        super.onCreate(savedInstanceState)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_today_menu -> {
                viewModel.updateFilter(Filter.TODAY)
            }
            R.id.show_save_menu -> {
                viewModel.updateFilter(Filter.SAVED)
            }
            R.id.show_week_menu -> {
                viewModel.updateFilter(Filter.WEEK)
            }
        }
        return true
    }
}
