package ru.mgprojects.validation

import org.springframework.stereotype.Component
import ru.mgprojects.dto.QuoteDTO
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component("quoteBidAskValidator")
class QuoteBidAskValidator : ConstraintValidator<QuoteBidAskValidation, QuoteDTO?> {

    override fun isValid(value: QuoteDTO?, context: ConstraintValidatorContext?): Boolean {
        return if (value == null) true else value.bid ?: 0.0 < value.ask
    }

}
