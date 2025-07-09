package io.hhplus.tdd.point

import io.hhplus.tdd.database.*
import io.hhplus.tdd.point.`PointServiceException 과 그 수하들`.*
import org.springframework.stereotype.Service

@Service
class PointServiceImpl(
    private val userPointTable: UserPointTable,
    private val pointHistoryTable: PointHistoryTable
): PointService {   
    /**
     * 특정 유저의 포인트를 조회합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 사용자의 포인트 정보
     * @throws IllegalArgumentException userId가 유효하지 않은 경우
     */
    override fun getUserPoint(userId: Long): UserPoint {
        userId.throwIfNotExistsUser()

        val userPointBalance = userPointTable.selectById(userId)

        return userPointBalance
    }

    /**
     * 특정 유저의 포인트를 충전합니다.
     * 
     * @param userId 충전할 사용자 ID
     * @param amount 충전할 금액
     * @return 충전 후 사용자의 포인트 정보
     */
    override fun chargePoint(userId: Long, amount: Long): UserPoint {
        userId.throwIfNotExistsUser()
        if (amount < 0) throw NegativeChargeAmountException()

        val originalPointBalance = userPointTable.selectById(userId)
        val newPoint = originalPointBalance.point + amount
        val updatedPointBalance = userPointTable.insertOrUpdate(
            id = userId,
            amount = newPoint)

        pointHistoryTable.insert(
            id = userId,
            amount = amount,
            transactionType = TransactionType.CHARGE,
            updateMillis = updatedPointBalance.updateMillis
        )

        return updatedPointBalance
    }   

    /**
     * 특정 유저의 포인트를 사용합니다.
     * 
     * @param userId 사용할 사용자 ID
     * @param amount 사용할 금액
     * @return 사용 후 사용자의 포인트 정보
     */
    override fun usePoint(userId: Long, amount: Long): UserPoint {
        userId.throwIfNotExistsUser()
        if (amount < 0) throw NegativeUsageException()

        val originalPointBalance = userPointTable.selectById(userId)

        if (originalPointBalance.point < amount) {
            throw NotEnoughPointException()
        }

        val newPoint = originalPointBalance.point - amount
        val updatedPointBalance = userPointTable.insertOrUpdate(
            id = userId,
            amount = newPoint
        )

        pointHistoryTable.insert(
            id = userId,
            amount = amount,
            transactionType = TransactionType.USE,
            updateMillis = updatedPointBalance.updateMillis)

        return updatedPointBalance
    }

    /**
     * 특정 유저의 포인트 내역을 조회합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 사용자의 포인트 내역
     */
    override fun getPointHistories(userId: Long): List<PointHistory> {
        userId.throwIfNotExistsUser()

        return pointHistoryTable.selectAllByUserId(userId)
    }

    /**
     * 유저 존재 여부 체크
     * 임시: 양수 유저 아이디만 존재하는 유저로 판단
     */
    private fun existsUser(userId: Long) : Boolean {
        return userId > 0
    }

    /**
     * 유저 존재 여부 체크하고 없으면 예외 발생
     */
    private fun Long.throwIfNotExistsUser() {
        if (!existsUser(this)) throw UserNotFoundException()
    }
}