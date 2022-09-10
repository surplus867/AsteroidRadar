package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.RetrofitInstance.retrofitService
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception


enum class ApiStatus { LOADING, ERROR, DONE }

enum class Filter { TODAY, WEEK, SAVED }

class MainViewModel: ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _navigationToDetail = MutableLiveData<Asteroid>()
    val navigationToDetail: LiveData<Asteroid>
        get() = _navigationToDetail

    private val _imageIOTD = MutableLiveData("")
    val imageIOTD: LiveData<String>
        get() = _imageIOTD

    init {
        viewModelScope.launch {
            getImage()
        }
        viewModelScope.launch {
            getAsteroids()
        }

    }

 /*   private val asteroidType = MutableLiveData(Filter.TODAY)

 *//*   fun updateFilter(filer: Filter) {
        asteroidType.value = filer
    }*/

    private suspend fun getImage() {
        _imageIOTD.value = retrofitService.getImageOfDay(Constants.API_KEY).body()!!.url
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigationToDetail.value = asteroid

    }

    fun onAsteroidNavigated() {
        _navigationToDetail.value = null
    }

    fun updateFilter(filter: Filter) {
        //TODO make call call to grab feed

    }

    suspend fun getAsteroids() {
        try {
            val startDate = "2022-08-08"
            val endDate = "2022-08-09"
            val result = retrofitService.getFeeds(startDate, endDate,  Constants.API_KEY)
            _asteroidList.value = parseAsteroidsJsonResult(JSONObject(result), startDate, endDate)
        } catch (e: Exception) {
            // handle error
            Log.d("getAsteroids Exception", "Exception: ${e.localizedMessage}")
        }



    }
    }
