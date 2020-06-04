package ru.mgprojects.controller

import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import ru.mgprojects.dto.QuoteDTO
import ru.mgprojects.enity.ElvlEntity
import ru.mgprojects.enity.QuoteEntity
import ru.mgprojects.service.QuoteService
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.validation.Valid


@RestController
@RequestMapping("/quote")
class QuoteController {

    val count = AtomicInteger(0)

    @Autowired
    lateinit var quoteService: QuoteService

    /**
     * Loading quotes(Загрузка котировок)
     */
    @ApiOperation(value = "/load", notes = "Loading quotes(Загрузка котировок)")
    @PostMapping("/load")
    fun load(
        @RequestBody @Valid quote: QuoteDTO
    ): ResponseEntity<QuoteEntity> {
        val attemptNumber = count.incrementAndGet()
        logger.info("$attemptNumber. Call method '/quote/load' with data: $quote")
        return ResponseEntity(quoteService.loadQuotes(quote), HttpStatus.OK)
    }

    /**
     * Getting energy level by isin(Получение лучшей цены по данному инструменту isin)
     */
    @ApiOperation(
        value = "/all",
        notes = "Getting energy level by isin(Получение лучшей цены по данному инструменту isin)"
    )
    @GetMapping("/all")
    fun getElvlByIsIn(): ResponseEntity<List<QuoteEntity>> {
        logger.info("Call method '/quote/all'")

        return ResponseEntity(quoteService.getAllQuotes(), HttpStatus.OK)
    }

    /**
     * Getting energy level by isin(Получение лучшей цены по данному инструменту isin)
     */
    @ApiOperation(
        value = "/elvl/{isin}",
        notes = "Getting energy level by isin(Получение лучшей цены по данному инструменту isin)"
    )
    @GetMapping("/elvl/{isin}")
    fun getElvlByIsIn(
        @PathVariable isin: String
    ): ResponseEntity<ElvlEntity> {
        logger.info("Call method '/quote/elvl/{isin}' with data: $isin")

        return ResponseEntity(quoteService.getElvlByIsin(isin) as ElvlEntity, HttpStatus.OK)
    }

    /**
     * Getting all energy levels(Получение всех лучших цен)
     */
    @ApiOperation(value = "/elvl/all", notes = "Getting all energy levels(Получение всех лучших цен)")
    @GetMapping("/elvl/all")
    fun getAllElvls(): ResponseEntity<List<ElvlEntity>> {
        logger.info("Call method '/elvl/all'")

        return ResponseEntity(quoteService.getAllElvls(), HttpStatus.OK)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(QuoteController::class.java)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        MethodArgumentNotValidException::class
    )
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): List<String?>? {
        val bindingResult = ex.bindingResult
        val fieldErrors = bindingResult.allErrors
        val errorList: MutableList<String?> =
            ArrayList(fieldErrors.size)
        fieldErrors.stream()
            .filter { obj: ObjectError? -> Objects.nonNull(obj) }
            .forEach { error: ObjectError ->
                val errorMessage = error.defaultMessage
                errorList.add(errorMessage)
                if (error is FieldError) {
                    val fieldName = error.field
                    logger.info("$fieldName: $errorMessage")
                }
            }
        return errorList
    }
}
