package com.clinic.dentClinic


import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(SpringRunner::class)
@WebMvcTest(ReservationController::class)
class ReservationControllerTest {

    @MockBean
    lateinit var reservationService: ReservationService

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun shouldGetAllReservations() {
        // given
        val givenDate = LocalDateTime.of(2019, 10, 18, 12, 30, 0)
        val givenDate2 = LocalDateTime.of(2019, 10, 18, 13, 0, 0)
        val givenReservation1 = Reservation("1", givenDate, Client("2", "Max"))
        val givenReservation2 = Reservation("3", givenDate2, Client("4", "Joe"))

        given(reservationService.getAllReservations()).willReturn(
                listOf(givenReservation1,
                        givenReservation2))
        // when
        mvc.perform(get("/reservations")
                // then
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id", equalTo(givenReservation1.id)))
                .andExpect(jsonPath("$[0].date", equalTo(givenReservation1.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].client.name", equalTo(givenReservation1.client.name)))
                .andExpect(jsonPath("$[0].client.id", equalTo(givenReservation1.client.id)))
                .andExpect(jsonPath("$[1].id", equalTo(givenReservation2.id)))
                .andExpect(jsonPath("$[1].date", equalTo(givenReservation2.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].client.name", equalTo(givenReservation2.client.name)))
                .andExpect(jsonPath("$[1].client.id", equalTo(givenReservation2.client.id)))
                .andReturn()
    }

    @Test
    fun shouldCreateReservation() {

        // given
        val givenDate = LocalDateTime.of(2019, 10, 21, 17, 0, 0)
        val createReservation = CreateReservation(givenDate, "1")
        val expectedReservation = Reservation("1", givenDate, Client("1", "John"))
        given(reservationService.createReservation(createReservation))
                .willReturn(expectedReservation)

        // when
        mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":\"1\"" +
                        ",\"date\":\"2019-10-21T17:00:00\"}")
                // then
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", equalTo(expectedReservation.id)))
                .andExpect(jsonPath("$.date", equalTo(expectedReservation.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.client.id", equalTo(expectedReservation.client.id)))
                .andExpect(jsonPath("$.client.name", equalTo(expectedReservation.client.name)))
    }

    @Test
    fun shouldHandleClientNotPresentException() {

        // given
        val givenDate = LocalDateTime.of(2019, 10, 21, 17, 0, 0)
        val createReservation = CreateReservation(givenDate, "1")
        given(reservationService.createReservation(createReservation))
                .willThrow(ClientNotPresentException::class.java)

        // when
        mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":\"1\"" +
                        ",\"date\":\"2019-10-21T17:00:00\"}")
                // then
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)// todo jaki kod HTTP?
                .andDo(MockMvcResultHandlers.print())
    }
}