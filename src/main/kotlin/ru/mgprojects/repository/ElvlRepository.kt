package ru.mgprojects.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.mgprojects.enity.ElvlEntity

@Repository
interface ElvlRepository : JpaRepository<ElvlEntity, String> {

    fun findByIsin(isin: String): List<ElvlEntity>
}