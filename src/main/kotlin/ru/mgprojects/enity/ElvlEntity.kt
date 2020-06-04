package ru.mgprojects.enity

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class ElvlEntity(
    @Id
    @Size(min = 12, max = 12)
    val isin: String,

    @Column(nullable = false)
    var elvl: Double
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ElvlEntity

        if (isin != other.isin) return false

        return true
    }

    override fun hashCode(): Int {
        return isin.hashCode()
    }
}