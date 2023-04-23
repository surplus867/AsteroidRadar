package com.udacity.asteroidradar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.getNextSevenDaysFormattedDates
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidDatabase) {
    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = startDate.plusDays(7)


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    val todayAsteroids = Transformations.map(
        database.asteroidDao.getAsteroidToday(getNextSevenDaysFormattedDates().first())) {
        it.asDomainModel()
    }


}