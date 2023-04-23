package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getAsteroids():LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate = :date ORDER BY closeApproachDate ASC")
    fun getAsteroidToday(date:String):LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidWeek(startDate:String,endDate:String):LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}