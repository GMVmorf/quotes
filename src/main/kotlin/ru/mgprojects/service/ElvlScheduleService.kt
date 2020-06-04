package ru.mgprojects.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.mgprojects.enity.CurrentQuoteIdEntity
import ru.mgprojects.enity.ElvlEntity
import ru.mgprojects.enity.QuoteEntity
import ru.mgprojects.repository.CurrentQuoteIdRepository
import ru.mgprojects.repository.ElvlRepository
import ru.mgprojects.repository.QuoteRepository

@Component
class ElvlScheduleService {

    @Autowired
    lateinit var quoteRepository: QuoteRepository

    @Autowired
    lateinit var elvlRepository: ElvlRepository

    @Autowired
    lateinit var currentQuoteIdRepository: CurrentQuoteIdRepository

    @Scheduled(fixedDelay = 3000)
    fun scheduleFixedDelayTask() {
        var currentQuote = currentQuoteIdRepository.findTopByOrderByCurrentQuoteIdDesc().firstOrNull()
        logger.debug("ElvlScheduleService.scheduleFixedDelayTask: currentQuoteId = $currentQuote")
        val lastInsertedQuote = quoteRepository.findTopByOrderByIdDesc().firstOrNull()
        logger.debug("ElvlScheduleService.scheduleFixedDelayTask: first quote: $lastInsertedQuote")

        while (currentQuote?.currentQuoteId != lastInsertedQuote?.id) {
            val currentQuoteId = currentQuote?.currentQuoteId ?: START_ID
            val nextQuote = quoteRepository.findTopByIdGreaterThan(currentQuoteId).firstOrNull()
            if (nextQuote == null) {
                logger.error(
                    "ElvlScheduleService.scheduleFixedDelayTask: There is no quote with id" +
                            " greater than ${currentQuote?.currentQuoteId}"
                )
            } else {
                val elvlEntity = elvlRepository.findByIsin(nextQuote.isin).firstOrNull()
                calculateElvl(nextQuote, elvlEntity?.elvl)
            }
            currentQuote = currentQuoteIdRepository.findTopByOrderByCurrentQuoteIdDesc().firstOrNull()
        }
    }

    /**
     * Function to calculate Elvl for current quote
     *
     * NB: Conditions:
     * 1. if bid > elvl than elvl = bid
     * 2. if ask < elvl than elvl = ask
     * 3. if elvl is null than elvl = bid
     * 4. if bid is null than elvl = ask
     * 5. bid is less than ask
     * According to conditions:
     * a. if bid is null THAN 1st condition is false
     * b. if elvl is null and bid is null THAN 3rd condition is false(otherwise, we ignoring ask value)
     * c. 5th condition THAN ask is always not null
     */
    @Transactional
    private fun calculateElvl(quote: QuoteEntity, elvl: Double?) {
        logger.debug("ElvlScheduleService.calculateElvl: quote: $quote, currentQuoteId: $elvl")

        val quoteElvl: Double? =
            if (quote.bid == null) quote.ask
            else if (elvl == null || quote.bid > elvl) quote.bid
            else if (quote.ask < elvl) quote.ask
            else elvl

        if (quoteElvl == null) {
            logger.error("ElvlScheduleService.calculateElvl: cannot calculate elvl for quote: $quote, elvl: $elvl")
        } else {
            saveElvl(quote, quoteElvl)
        }
    }

    private fun saveElvl(quote: QuoteEntity, quoteElvl: Double) {
        val elvlEntity = elvlRepository.findByIsin(quote.isin).lastOrNull() ?: ElvlEntity(quote.isin, quoteElvl)
        elvlEntity.elvl = quoteElvl
        val savedElvlEntity = elvlRepository.save(elvlEntity)
        logger.debug("ElvlScheduleService.calculateElvl: savedElvlEntity: $savedElvlEntity")

        val savedCurrentQuoteIdEntity = currentQuoteIdRepository.save(CurrentQuoteIdEntity(quote.id!!))
        logger.debug("ElvlScheduleService.calculateElvl: savedCurrentQuoteIdEntity: $savedCurrentQuoteIdEntity")
    }

    companion object {
        val logger = LoggerFactory.getLogger(ElvlScheduleService::class.java)

        const val START_ID: Long = 0
    }
}