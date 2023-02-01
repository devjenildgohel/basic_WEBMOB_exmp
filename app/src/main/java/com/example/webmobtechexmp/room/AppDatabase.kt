package com.example.webmobtechexmp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.webmobtechexmp.model.User

@Database(entities = [User::class], version = 3)
@TypeConverters(
    NameConverter::class,
    LocationConverter::class,
    DobConverter::class,
    LoginTypeConverter::class,
    RegisteredTypeConverter::class,
    PictureConverter::class,
    IdConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
