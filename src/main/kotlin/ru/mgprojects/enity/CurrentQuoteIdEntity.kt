package ru.mgprojects.enity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CurrentQuoteIdEntity(
    @Id
    val currentQuoteId: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrentQuoteIdEntity

        if (currentQuoteId != other.currentQuoteId) return false

        return true
    }

    override fun hashCode(): Int {
        return currentQuoteId.hashCode()
    }
}