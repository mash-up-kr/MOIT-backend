package com.mashup.moit.domain.moit.converter

import com.mashup.moit.domain.user.UserRole
import jakarta.persistence.AttributeConverter

class UserRoleConverter : AttributeConverter<Set<UserRole>, String> {
    override fun convertToDatabaseColumn(attribute: Set<UserRole>): String {
        return attribute.joinToString(DELIMITER) { it.name }
    }

    override fun convertToEntityAttribute(dbData: String): Set<UserRole> {
        return dbData.split(DELIMITER).map { UserRole.valueOf(it.trim()) }.toSet()
    }

    companion object {
        private const val DELIMITER = ","
    }
}
