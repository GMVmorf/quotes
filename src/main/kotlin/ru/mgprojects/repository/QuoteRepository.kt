package ru.mgprojects.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.mgprojects.enity.QuoteEntity

@Repository
interface QuoteRepository : JpaRepository<QuoteEntity, Long> {

    fun findByIsin(isin: String): List<QuoteEntity>

    fun findTopByOrderByIdDesc(): List<QuoteEntity>

    fun findTopByIdGreaterThan(id: Long): List<QuoteEntity>
}