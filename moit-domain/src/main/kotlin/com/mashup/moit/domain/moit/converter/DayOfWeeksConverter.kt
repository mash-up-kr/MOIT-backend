package com.mashup.moit.domain.moit.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.DayOfWeek

@Converter
class DayOfWeeksConverter : AttributeConverter<Set<DayOfWeek>, String> {
    override fun convertToDatabaseColumn(attribute: Set<DayOfWeek>): String {
        return attribute.joinToString(DELIMITER) { it.name }
    }

    override fun convertToEntityAttribute(dbData: String): Set<DayOfWeek> {
        return dbData.split(DELIMITER).map { DayOfWeek.valueOf(it) }.toSet()
    }

    companion object {
        private const val DELIMITER = ","
    }
}
