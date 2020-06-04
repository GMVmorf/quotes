package ru.mgprojects.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mgprojects.dto.QuoteDTO
import ru.mgprojects.enity.ElvlEntity
import ru.mgprojects.enity.QuoteEntity
import ru.mgprojects.repository.CurrentQuoteIdRepository
import ru.mgprojects.repository.ElvlRepository
import ru.mgprojects.repository.QuoteRepository

@Service
class QuoteService {

    @Autowired
    lateinit var quoteRepository: QuoteRepository

    @Autowired
    lateinit var elvlRepository: ElvlRepository

    @Autowired
    lateinit var currentQuoteIdRepository: CurrentQuoteIdRepository

    @Transactional
    fun loadQuotes(quoteDTO: QuoteDTO): QuoteEntity {
        return quoteRepository.save(quoteDTO.toQuoteEntity())
    }

    @Transactional(readOnly = true)
    fun getAllElvls(): List<ElvlEntity> {
        return elvlRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getElvlByIsin(isin: String): ElvlEntity? {
        return elvlRepository.findByIsin(isin).firstOrNull()
    }

    @Transactional(readOnly = true)
    fun getAllQuotes(): List<QuoteEntity> {
        return quoteRepository.findAll()
    }

    companion object {
        const val QUOTES_SUCCESSFULLY_LOADED = "Quotes successfully loaded"
    }
}