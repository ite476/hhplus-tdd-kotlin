package io.hhplus.tdd.point

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch

@WebMvcTest(PointController::class)
class PointControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `포인트 조회 - 정상 조회`() {
        // given
        val userId = 1L
        val expectedPoint = 1000L

        // when & then
        mockMvc.perform(get("/point/$userId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.point").value(expectedPoint))
            .andExpect(jsonPath("$.updateMillis").exists())
    }

    @Test
    fun `포인트 충전 - 정상 충전`() {
        // given
        val userId = 1L
        val amount = 1000L

        // when & then
        mockMvc.perform(patch("/point/$userId/charge")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(amount)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.point").value(amount))
            .andExpect(jsonPath("$.updateMillis").exists())
    }
} 