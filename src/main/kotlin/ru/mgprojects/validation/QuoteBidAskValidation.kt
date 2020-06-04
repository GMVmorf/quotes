package ru.mgprojects.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [QuoteBidAskValidator::class])
annotation class QuoteBidAskValidation constructor(
    val message: String = "'Bid' must be less than 'Ask'",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
