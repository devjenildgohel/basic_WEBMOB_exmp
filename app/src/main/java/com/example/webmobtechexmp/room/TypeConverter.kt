package com.example.webmobtechexmp.room

import androidx.room.TypeConverter
import com.example.webmobtechexmp.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.IDN


class NameConverter {
    @TypeConverter
    fun fromName(name: Name): String {
        return "${name.first},${name.last}"
    }

    @TypeConverter
    fun toName(data: String): Name {
        val parts = data.split(",")
        return Name(" ",parts[0], parts[1])
    }
}

class LocationConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocation(location: Location): String {
        val type = object : TypeToken<Location>() {}.type
        return gson.toJson(location, type)
    }

    @TypeConverter
    fun toLocation(location: String): Location {
        val type = object : TypeToken<Location>() {}.type
        return gson.fromJson(location, type)
    }
}

class DobConverter {
    @TypeConverter
    fun fromDob(dob: Dob): String {
        return dob.date + "," + dob.age
    }

    @TypeConverter
    fun toDob(value: String): Dob {
        val values = value.split(",")
        return Dob(values[0], values[1].toInt())
    }
}

class LoginTypeConverter {

    @TypeConverter
    fun fromLogin(value: Login): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLogin(value: String): Login {
        val type = object : TypeToken<Login>() {}.type
        return Gson().fromJson(value, type)
    }
}

class RegisteredTypeConverter {
    @TypeConverter
    fun fromRegistered(registered: Registered): String {
        val gson = Gson()
        val type = object : TypeToken<Registered>() {}.type
        return gson.toJson(registered, type)
    }

    @TypeConverter
    fun toRegistered(registeredString: String): Registered {
        val gson = Gson()
        val type = object : TypeToken<Registered>() {}.type
        return gson.fromJson(registeredString, type)
    }
}

class PictureConverter {
    @TypeConverter
    fun fromPicture(picture: Picture): String {
        return Gson().toJson(picture)
    }

    @TypeConverter
    fun toPicture(picture: String): Picture {
        return Gson().fromJson(picture, Picture::class.java)
    }
}

class IdConverter {
    @TypeConverter
    fun fromId(id: Id): String {
        return Gson().toJson(id)
    }

    @TypeConverter
    fun toId(id: String): Id {
        return Gson().fromJson(id, Id::class.java)
    }
}






