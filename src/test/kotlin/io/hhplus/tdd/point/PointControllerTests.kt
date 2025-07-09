package io.hhplus.tdd.point

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PointControllerTests {
    @Test
    fun `포인트 조회 - 정상 조회`() {
        // given
        val userId = 1L
        val expectedUserPoint = UserPoint(
            userId,
            1000L,
            111L,
        )

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.getUserPoint(userId)
        } returns expectedUserPoint

        val sutController = PointController(mockPointService)

        // when
        val response: ResponseEntity<UserPoint> = sutController.point(userId)

        // then
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe expectedUserPoint
    }

    @Test
    fun `포인트 변경 내역 조회 - 정상 조회`(){
        // given
        val userId = 1L
        val expectedHistory = listOf(
            PointHistory(
                id = 1L,
                userId = userId,
                type = TransactionType.CHARGE,
                amount = 15_000L,
                timeMillis = 1_000_000L
            ),
            PointHistory(
                id = 2L,
                userId = userId,
                type = TransactionType.USE,
                amount = 5_000L,
                timeMillis = 1_100_000L
            ),
        )

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.getPointHistories(userId)
        } returns expectedHistory

        val sutController = PointController(mockPointService)

        // when
        val response = sutController.history(userId)

        // then
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe expectedHistory
    }

    @Test
    fun `포인트 충전 - 정상 충전`() {
        // given
        val userId = 1L
        val pointInput = 1_000L
        val expectedUserPoint = UserPoint(
            id = userId,
            point = pointInput,
            updateMillis = 1_000_000L
        )

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.chargePoint(userId, pointInput)
        } returns expectedUserPoint

        val sutController = PointController(mockPointService)

        // when
        val response = sutController.charge(userId, pointInput)

        // then
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe expectedUserPoint
    }

    @Test
    fun `포인트 사용 - 정상 사용`() {
        // given
        val userId = 1L
        val pointBalance = 10_000L
        val pointUsage = 5_000L

        val expectedUserPoint = UserPoint(
            id = userId,
            point = pointBalance - pointUsage,
            updateMillis = 1_000_000L
        )

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.usePoint(userId, pointUsage)
        } returns expectedUserPoint

        val sutController = PointController(mockPointService)

        // when
        val response = sutController.use(userId, pointUsage)

        // then
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe expectedUserPoint
    }
}