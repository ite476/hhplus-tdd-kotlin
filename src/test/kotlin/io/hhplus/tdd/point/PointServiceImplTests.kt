package io.hhplus.tdd.point

import io.hhplus.tdd.database.*
import io.hhplus.tdd.point.`PointServiceException 과 그 수하들`.*
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.*

class PointServiceImplTests {
    @Test
    fun `포인트 조회 - 정상 조회`() {
        // given
        val userId = 1L

        val expectedPoint = 1_000_000L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )
        run {
            sutService.chargePoint(userId, expectedPoint)
        }

        // when
        val userPointBalance = sutService.getUserPoint(userId)

        // then
        userPointBalance.let { it ->
            it.id shouldBe userId
            it.point shouldBe expectedPoint
        }
    }

    @Test
    fun `포인트 조회 - 사용자 없음`() {
        // given
        val userId = -777_777L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<UserNotFoundException> {
            sutService.getUserPoint(userId)
        }
    }

    @Test
    fun `포인트 변경 내역 조회 - 정상 조회`(){
        // given
        val userId = 1L
        // 관심 외 변수
        // SMELL: 임시 값 하드코딩
        val timeMillis = 0L

        val expectedHistory = listOf(
            PointHistory(
                id = 1L,
                userId = userId,
                type = TransactionType.CHARGE,
                amount = 15_000L,
                timeMillis = timeMillis
            ),
            PointHistory(
                id = 2L,
                userId = userId,
                type = TransactionType.USE,
                amount = 5_000L,
                timeMillis = timeMillis
            ),
        )

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )
        run {
            expectedHistory.forEach { change ->
                if (change.type == TransactionType.USE) {
                    sutService.usePoint(
                        userId = change.userId,
                        amount = change.amount)
                }
                else {
                    sutService.chargePoint(
                        userId = change.userId,
                        amount = change.amount
                    )
                }
            }
        }

        // when
        val histories = sutService.getPointHistories(userId)

        // then
        histories.zip(expectedHistory)
        .forEach { pair ->
            val actual = pair.first
            val expected = pair.second

            actual.let { it ->
                it.id shouldBe expected.id
                it.userId shouldBe expected.userId
                it.type shouldBe expected.type
                it.amount shouldBe expected.amount
            }
        }
    }

    @Test
    fun `포인트 변경 내역 조회 - 사용자 없음`() {
        // given
        val userId = -999_999L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<UserNotFoundException> {
            sutService.getPointHistories(userId)
        }
    }

    @Test
    fun `포인트 충전 - 정상 충전`() {
        // given
        val userId = 1L

        val expectedPoint = 15_000L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )

        // when
        val updatedPointBalance = sutService.chargePoint(
            userId = userId,
            amount = expectedPoint)

        // then
        updatedPointBalance.let { it ->
            it.id shouldBe userId
            it.point shouldBe expectedPoint
        }
    }

    @Test
    fun `포인트 충전 - 사용자 없음`() {
        val userId = -999_999_999L
        val pointInput = 999_999_999L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<UserNotFoundException> {
            sutService.chargePoint(
                userId = userId,
                amount = pointInput
            )
        }
    }

    @Test
    fun `포인트 충전 - 음수 포인트 입력됨`() {
        // given
        val userId = 1L
        val pointInput = -999_999_999L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<NegativeChargeAmountException> {
            sutService.chargePoint(
                userId = userId,
                amount = pointInput
            )
        }
    }

    @Test
    fun `포인트 사용 - 정상 사용`() {
        // given
        val userId = 1L
        val originalPoint = 12_000L
        val pointUsage = 5_000L

        val expectedPoint = 7_000L

        val spyUserPointTable = spyk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            spyUserPointTable,
            spyPointHistoryTable
        )
        run {
            sutService.chargePoint(userId, originalPoint)
        }

        // when
        val updatedBalance = sutService.usePoint(
            userId = userId,
            amount = pointUsage
        )

        // then
        updatedBalance.let { it ->
            it.id shouldBe userId
            it.point shouldBe expectedPoint
        }
    }

    @Test
    fun `포인트 사용 - 사용자 없음`() {
        val userId = -999_999_999L
        val pointUsage = 999_999_999L

        val mockUserPointTable = mockk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            mockUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<UserNotFoundException> {
            sutService.usePoint(
                userId = userId,
                amount = pointUsage)
        }
    }

    @Test
    fun `포인트 사용 - 음수 포인트 사용 시도`() {
        val userId = 1L
        val pointUsage = -999_999_999L

        val mockUserPointTable = mockk<UserPointTable>()
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            mockUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<NegativeUsageException> {
            sutService.usePoint(
                userId = userId,
                amount = pointUsage)
        }
    }

    @Test
    fun `포인트 사용 - 잔고를 초과하는 포인트 사용 시도`() {
        val userId = 1L
        val pointUsage = 999_999_999L

        val originalBalance = UserPoint(
            id = userId,
            point = 5_000L,
            updateMillis = 1_234_567L
        )

        val mockUserPointTable = mockk<UserPointTable>()
        run {
            every {
                mockUserPointTable.selectById(userId)
            } returns originalBalance
        }
        val spyPointHistoryTable = spyk<PointHistoryTable>()

        val sutService = PointServiceImpl(
            mockUserPointTable,
            spyPointHistoryTable
        )

        // when & then
        assertThrows<NotEnoughPointException> {
            sutService.usePoint(
                userId = userId,
                amount = pointUsage)
        }
    }
}