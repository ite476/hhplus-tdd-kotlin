package io.hhplus.tdd.point

/**
 * 포인트 관리 서비스 인터페이스
 * 
 * 포인트 조회, 충전, 사용, 내역 조회 등의 비즈니스 로직을 담당합니다.
 * Clean Architecture 원칙에 따라 Controller와 Repository 계층을 분리합니다.
 */
interface PointService {
    
    /**
     * 특정 유저의 포인트를 조회합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 사용자의 포인트 정보
     * @throws IllegalArgumentException userId가 유효하지 않은 경우
     */
    fun getUserPoint(userId: Long): UserPoint
    
    /**
     * 특정 유저의 포인트를 충전합니다.
     * 
     * @param userId 충전할 사용자 ID
     * @param amount 충전할 금액 (양수여야 함)
     * @return 충전 후 사용자의 포인트 정보
     * @throws IllegalArgumentException amount가 음수이거나 userId가 유효하지 않은 경우
     * @throws IllegalStateException 충전 처리 중 오류가 발생한 경우
     */
    fun chargePoint(userId: Long, amount: Long): UserPoint
    
    /**
     * 특정 유저의 포인트를 사용합니다.
     * 
     * @param userId 사용할 사용자 ID
     * @param amount 사용할 금액 (양수여야 함)
     * @return 사용 후 사용자의 포인트 정보
     * @throws IllegalArgumentException amount가 음수이거나 userId가 유효하지 않은 경우
     * @throws IllegalStateException 잔액이 부족하거나 사용 처리 중 오류가 발생한 경우
     */
    fun usePoint(userId: Long, amount: Long): UserPoint
    
    /**
     * 특정 유저의 포인트 충전/사용 내역을 조회합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 포인트 내역 목록 (최신순)
     * @throws IllegalArgumentException userId가 유효하지 않은 경우
     */
    fun getPointHistories(userId: Long): List<PointHistory>
}
