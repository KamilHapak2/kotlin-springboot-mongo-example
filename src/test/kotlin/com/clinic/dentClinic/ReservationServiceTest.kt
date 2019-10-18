package com.clinic.dentClinic

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class ReservationServiceTest {

    private val clientRepository = mock(ClientRepository::class.java)
    private val reservationRepository = mock(ReservationRepository::class.java)

    private val reservationService = ReservationService(reservationRepository, clientRepository)

    @AfterEach
    internal fun tearDown() {
        reset(clientRepository, reservationRepository)
    }

    @Test
    internal fun shouldThrowExceptionWhenClientNotFound() {

        // given
        val givenClientId = "1"
        val givenDate = LocalDateTime.of(2019, 10, 17, 12, 0)
        `when`(clientRepository.findById(givenClientId)).thenReturn(Optional.empty())
        val createReservationDTO = CreateReservation(givenDate, "2")

        // then
        assertThrows<ClientNotPresentException> {
            // when
            reservationService.createReservation(createReservationDTO)
        }
    }

    @Test
    internal fun shouldCreateReservation() {

        // given
        val givenClientId = "1"
        val givenDate = LocalDateTime.of(2019, 10, 17, 12, 0)
        val givenClient = Client("1", "Joe")
        val expectedReservation = Reservation("1", givenDate, givenClient)
        `when`(clientRepository.findById(givenClientId)).thenReturn(Optional.of(givenClient))
        `when`(reservationRepository.save(any(Reservation::class.java)))
                .thenReturn(Reservation("1", givenDate, givenClient))
        val createReservationDTO = CreateReservation(givenDate, givenClientId)

        // when
        val actualReservation: Reservation = reservationService.createReservation(createReservationDTO)

        // then
        assertEquals(expectedReservation, actualReservation)
    }
}