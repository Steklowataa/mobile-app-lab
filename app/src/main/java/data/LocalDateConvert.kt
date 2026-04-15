package pl.oleksandra.pam.lab06.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    companion object {
        const val pattern = "yyyy-MM-dd"

        // Konwersja z Long (milisekundy) na LocalDate
        fun fromMillis(millis: Long): LocalDate {
            return Instant
                .ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        // Konwersja z LocalDate na Long (milisekundy)
        fun toMillis(date: LocalDate): Long {
            return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }

    // Te metody są dla Room (Baza danych <-> Tekst)
    @TypeConverter
    fun fromDateTime(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toDateTime(str: String?): LocalDate? {
        return str?.let { LocalDate.parse(it, formatter) }
    }
}