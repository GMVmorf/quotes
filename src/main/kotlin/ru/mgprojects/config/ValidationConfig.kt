package ru.mgprojects.config

import org.springframework.context.annotation.Bean
import ru.mgprojects.validation.QuoteBidAskValidator

class ValidationConfig {
    @Bean
    fun quoteBidAskValidator() = QuoteBidAskValidator()
}
