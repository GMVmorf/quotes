package ru.mgprojects.dto

import org.springframework.validation.annotation.Validated
import ru.mgprojects.enity.QuoteEntity
import ru.mgprojects.validation.QuoteBidAskValidation
import javax.validation.constraints.Size

@Validated
@QuoteBidAskValidation
data class QuoteDTO(
    @field: Size(min = 12, max = 12)
    val isin: String,

    val bid: Double?,

    val ask: Double
) {
    fun toQuoteEntity() = QuoteEntity(isin = this.isin, bid = this.bid, ask = this.ask)
}
