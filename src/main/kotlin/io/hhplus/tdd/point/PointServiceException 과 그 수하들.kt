package io.hhplus.tdd.point.`PointServiceException 과 그 수하들`

/**
 * PointService 내에서 발생 할 수 있는 케이스 예외들
 * @see PointSerivice <<< 이놈 보래요
 */
open class PointServiceException : Exception() {
}

/**
 * 유저가 서비스 상에 존재하지 않음
 */
class UserNotFoundException: PointServiceException() { }

/**
 * 음수 포인트 충전은 불가능함
 */
class NegativeChargeAmountException: PointServiceException() { }

/**
 * 음수 포인트 사용은 불가능함
 */
class NegativeUsageException: PointServiceException() { }

/**
 * 포인트 잔고가 부족함
 */
class NotEnoughPointException: PointServiceException() { }

