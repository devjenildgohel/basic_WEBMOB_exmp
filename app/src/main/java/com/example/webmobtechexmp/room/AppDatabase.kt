package com.example.webmobtechexmp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.webmobtechexmp.model.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
