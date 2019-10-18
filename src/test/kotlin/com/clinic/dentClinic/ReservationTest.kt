package com.clinic.dentClinic

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class ReservationTest {

    @Test
    internal fun shouldCreateReservation() {
        // when
        val reservation = Reservation(date = LocalDateTime.of(2019, 10, 17, 12, 0),
                client = Client(name = "Joe"))

        // then
        assertTrue(reservation.client.name == "Joe")
        assertTrue(reservation.date == LocalDateTime.of(2019, 10, 17, 12, 0))
    }
}