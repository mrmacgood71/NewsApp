package it.macgood.newsappapi.database

import androidx.room.TypeConverter
import it.macgood.newsappapi.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}