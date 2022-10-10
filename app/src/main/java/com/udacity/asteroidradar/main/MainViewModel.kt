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
import java.text.SimpleDateFormat
import java.util.*


enum class ApiStatus { LOADING, ERROR, DONE }

enum class Filter { TODAY, WEEK, SAVED }

class MainViewModel() : ViewModel() {
    /*val dataBase = getDatabase(application.applicationContext)*/

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _navigationToDetail = MutableLiveData<Asteroid?>()
    val navigationToDetail: MutableLiveData<Asteroid?>
        get() = _navigationToDetail

    private val _imageIOTD = MutableLiveData("")
    val imageIOTD: LiveData<String>
        get() = _imageIOTD

    init {
        viewModelScope.launch {
            getAsteroids()
            getImage()
        }

    }

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

    private suspend fun getAsteroids() {
        try {
            val today = Calendar.getInstance()
            val afterSevenDays = Calendar.getInstance().also { it.add(Calendar.DAY_OF_YEAR, 7) }
            val result = retrofitService.getAsteroids(
                SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date(today.timeInMillis)),
                SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date(afterSevenDays.timeInMillis)), Constants.API_KEY)
            _asteroidList.value = parseAsteroidsJsonResult(JSONObject(result))
            /*// todo convert List of Asteroid ito database list using the functions in DataBaseEntities so that it can be stored
            dataBase.asteroidDao.insertAll()*/

        } catch (e: Exception) {
            // handle error
            Log.d("getAsteroids Exception", "Exception: ${e.localizedMessage}")
        }
    }
    }