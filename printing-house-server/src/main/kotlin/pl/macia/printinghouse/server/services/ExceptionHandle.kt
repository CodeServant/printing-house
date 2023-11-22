package pl.macia.printinghouse.server.services

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.TransactionSystemException
import org.springframework.web.server.ResponseStatusException

/**
 * Catch exception from [action], prepare it and returns [ResponseStatusException] to a Controller.
 */
fun <E> changeExceptionCatch(action: () -> ResponseEntity<E>): ResponseEntity<E> {
    try {
        return action()
    } catch (e: TransactionSystemException) {
        val root = e.rootCause
        var causes = ""
        if (root is ConstraintViolationException) {
            root.constraintViolations.forEach {
                causes += "${it.propertyPath} ${it.message}, "
            }
            causes = causes.removeSuffix(", ")
        }
        throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            causes,
            e.rootCause
        )
    }
}