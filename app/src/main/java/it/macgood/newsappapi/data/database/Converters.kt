package it.macgood.newsappapi.data.database

import androidx.room.TypeConverter
import it.macgood.newsappapi.domain.model.Source

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