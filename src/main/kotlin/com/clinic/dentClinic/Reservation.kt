package com.clinic.dentClinic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@Document
data class Reservation(@Id var id: String? = null, val date: LocalDateTime, val client: Client)

@Document
data class Client(@Id var id: String? = null, val name: String)

data class CreateReservation(val date: LocalDateTime, val clientId: String)

@Repository
interface ReservationRepository : CrudRepository<Reservation, String>

class ClientNotPresentException(message: String) : RuntimeException(message)

@Repository
interface ClientRepository : CrudRepository<Client, String>

@Service
class ReservationService(@Autowired
                         private val reservationRepository: ReservationRepository,
                         @Autowired private val clientRepository: ClientRepository) {

    fun createReservation(createReservation: CreateReservation): Reservation {
        val client = clientRepository.findById(createReservation.clientId)
                .orElseThrow {
                    throw ClientNotPresentException("Client with id ${createReservation.clientId} does not exist!")
                }
        val reservation = Reservation(date = createReservation.date, client = client)
        return reservationRepository.save(reservation)
    }

    fun getAllReservations() = reservationRepository.findAll().toList()
}


@RestController
@RequestMapping("/reservations")
class ReservationController(@Autowired val reservationService: ReservationService) {

    @GetMapping
    fun getAllReservations() = reservationService.getAllReservations()

    @PostMapping
    fun createReservation(@RequestBody createReservation: CreateReservation) = reservationService.createReservation(createReservation)
}

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ClientNotPresentException::class)
    fun handleException(exception: RuntimeException, request: WebRequest) = handleExceptionInternal(exception, exception.message, HttpHeaders(), HttpStatus.NOT_FOUND, request)
}

