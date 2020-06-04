package ru.mgprojects.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.mgprojects.enity.CurrentQuoteIdEntity
import ru.mgprojects.enity.QuoteEntity

@Repository
interface CurrentQuoteIdRepository : JpaRepository<CurrentQuoteIdEntity, Long> {

    fun findTopByOrderByCurrentQuoteIdDesc(): List<CurrentQuoteIdEntity>
}