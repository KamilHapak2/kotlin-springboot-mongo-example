package com.clinic.dentClinic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@DataMongoTest()
@ExtendWith(SpringExtension::class)
class ClientRepositoryTest {

    @Test
    internal fun shouldSaveReservation(@Autowired clientRepository: ClientRepository) {

        // given
        val client = Client(name = "Joe")

        // when
        clientRepository.save(client)

        // then
        val clients = clientRepository.findAll()
        assertThat(clients)
                .extracting("id")
                .isNotNull()

        assertThat(clients)
                .extracting("name")
                .containsOnly("Joe")
    }
}