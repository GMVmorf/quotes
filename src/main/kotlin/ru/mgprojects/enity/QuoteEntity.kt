package ru.mgprojects.enity

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class QuoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Size(min = 12, max = 12)
    @Column(nullable = false)
    val isin: String,

    @Column(nullable = true)
    val bid: Double?,

    @Column(nullable = false)
    val ask: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuoteEntity

        if (id != other.id) return false
        if (isin != other.isin) return false
        if (bid != other.bid) return false
        if (ask != other.ask) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + isin.hashCode()
        result = 31 * result + (bid?.hashCode() ?: 0)
        result = 31 * result + ask.hashCode()
        return result
    }
}