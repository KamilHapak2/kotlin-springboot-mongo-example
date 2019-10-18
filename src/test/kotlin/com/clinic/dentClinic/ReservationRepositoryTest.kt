package com.clinic.dentClinic

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime


@DataMongoTest()
@ExtendWith(SpringExtension::class)
class ReservationRepositoryTest {

    @Test
    internal fun shouldSaveReservation(@Autowired reservationRepository: ReservationRepository) {

        // given
        val reservation = Reservation(date = LocalDateTime.of(2019, 10, 17, 12, 0),
                client = Client(name = "Joe"))

        // when
        reservationRepository.save(reservation)

        // then
        val allReservations = reservationRepository.findAll()
        assertThat(allReservations)
                .extracting("id", "client.id")
                .isNotNull()
        assertThat(allReservations)
                .extracting("date", "client.name")
                .containsOnly(tuple(LocalDateTime.of(2019, 10, 17, 12, 0), "Joe"))
    }
}