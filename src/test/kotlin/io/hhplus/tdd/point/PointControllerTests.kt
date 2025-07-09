package io.hhplus.tdd.point

import io.hhplus.tdd.point.`PointServiceException 과 그 수하들`.*
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.*
import org.springframework.http.*
import org.springframework.web.server.ResponseStatusException

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
    fun `포인트 조회 - 사용자 없음`() {
        val userId = 1L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.getUserPoint(userId)
        } throws UserNotFoundException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.point(userId)
        }

        caughtException.statusCode shouldBe HttpStatus.CONFLICT
        caughtException.body.detail shouldBe "회원이 존재하지 않습니다."
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
    fun `포인트 변경 내역 조회 - 사용자 없음`() {
        val userId = 1L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.getPointHistories(userId)
        } throws UserNotFoundException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.history(userId)
        }

        caughtException.statusCode shouldBe HttpStatus.CONFLICT
        caughtException.body.detail shouldBe "회원이 존재하지 않습니다."
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
    fun `포인트 충전 - 사용자 없음`() {
        val userId = 1L
        val pointInput = 999_999_999L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.chargePoint(userId, pointInput)
        } throws UserNotFoundException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.charge(userId, pointInput)
        }

        caughtException.statusCode shouldBe HttpStatus.CONFLICT
        caughtException.body.detail shouldBe "회원이 존재하지 않습니다."
    }

    @Test
    fun `포인트 충전 - 음수 포인트 입력됨`() {
        val userId = 1L
        val pointInput = 999_999_999L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.chargePoint(userId, pointInput)
        } throws NegativeChargeAmountException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.charge(userId, pointInput)
        }

        caughtException.statusCode shouldBe HttpStatus.UNPROCESSABLE_ENTITY
        caughtException.body.detail shouldBe "포인트 충전은 0 이상의 정수만 가능합니다."
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

    @Test
    fun `포인트 사용 - 사용자 없음`() {
        val userId = 1L
        val pointInput = 999_999_999L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.usePoint(userId, pointInput)
        } throws UserNotFoundException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.use(userId, pointInput)
        }

        caughtException.statusCode shouldBe HttpStatus.CONFLICT
        caughtException.body.detail shouldBe "회원이 존재하지 않습니다."
    }

    @Test
    fun `포인트 사용 - 음수 포인트 사용 시도`() {
        val userId = 1L
        val pointInput = -999_999_999L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.usePoint(userId, pointInput)
        } throws NegativeUsageException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.use(userId, pointInput)
        }

        caughtException.statusCode shouldBe HttpStatus.UNPROCESSABLE_ENTITY
        caughtException.body.detail shouldBe "포인트 사용은 0 이상의 정수만 가능합니다."
    }

    @Test
    fun `포인트 사용 - 잔고를 초과하는 포인트 사용 시도`() {
        val userId = 1L
        val userPointBalance = 1_000L
        val pointInput = 999_999_999L

        val mockPointService = mockk<PointService>()
        every {
            mockPointService.usePoint(userId, pointInput)
        } throws NotEnoughPointException()

        val sutController = PointController(mockPointService)

        // when & then
        val caughtException = assertThrows<ResponseStatusException> {
            sutController.use(userId, pointInput)
        }

        caughtException.statusCode shouldBe HttpStatus.CONFLICT
        caughtException.body.detail shouldBe "사용할 수 있는 포인트가 부족합니다."
    }
}