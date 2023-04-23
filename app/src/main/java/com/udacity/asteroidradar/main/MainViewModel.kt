package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.api.RetrofitInstance.retrofitService
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


enum class ApiStatus { LOADING, ERROR, DONE }

enum class Filter { TODAY, WEEK, SAVED }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    // startDate
    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    // endDate
    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().minusDays(7)

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _navigationToDetail = MutableLiveData<Asteroid?>()
    val navigationToDetail: MutableLiveData<Asteroid?>
        get() = _navigationToDetail

    private val _imageIOTD = MutableLiveData("")
    val imageIOTD: LiveData<String>
        get() = _imageIOTD

    // All Asteroids
    val allAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    // Week Asteroids
    @RequiresApi(Build.VERSION_CODES.O)
    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidWeek(
            startDate.format(DateTimeFormatter.ISO_DATE),
            endDate.format(DateTimeFormatter.ISO_DATE)
        )) {
            it.asDomainModel()
        }

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
            val test = parseAsteroidsJsonResult(JSONObject(result))
            // todo convert List of Asteroid ito database list using the functions in DataBaseEntities so that it can be stored
            viewModelScope.launch(Dispatchers.IO) {
                database.asteroidDao.insertAll(*test.asDatabaseModel())
                Log.d("Bilbo", "is database empty: ${repository.asteroids.value.orEmpty().isEmpty()}")
                Log.d("Bilbo", "first item in list: ${repository.asteroids.value.orEmpty().firstOrNull()}")
                Log.d("Refresh Asteroids", "Success")
            }
        } catch (e: Exception) {
            // handle error
            Log.d("getAsteroids Exception", "Exception: ${e.localizedMessage}")
        }
    }
    }