package io.hhplus.tdd.point

import io.hhplus.tdd.point.`PointServiceException 과 그 수하들`.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/point")
@Tag(name = "Point API", description = "포인트 관리 API")
class PointController(
    private val pointService: PointService
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("{id}")
    @Operation(summary = "포인트 조회", description = "특정 유저의 포인트를 조회합니다.")
    fun point(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
    ): ResponseEntity<UserPoint> {
        try{
            val userPointBalance = pointService.getUserPoint(id)

            return ResponseEntity
                .ok(userPointBalance)
        }
        catch (ex : UserNotFoundException){
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "회원이 존재하지 않습니다.")
        }
    }

    @GetMapping("{id}/histories")
    @Operation(summary = "포인트 내역 조회", description = "특정 유저의 포인트 충전/사용 내역을 조회합니다.")
    fun history(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
    ): ResponseEntity<List<PointHistory>> {
        try{
            val userPointHistories = pointService.getPointHistories(id)

            return ResponseEntity
                .ok(userPointHistories)
        }catch (ex: UserNotFoundException){
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "회원이 존재하지 않습니다.")
        }
    }

    @PatchMapping("{id}/charge")
    @Operation(summary = "포인트 충전", description = "특정 유저의 포인트를 충전합니다.")
    fun charge(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
        @Parameter(description = "충전할 금액") 
        @RequestBody 
        amount: Long,
    ): ResponseEntity<UserPoint> {
        try{
            val chargedUserPointBalance = pointService.chargePoint(id, amount)

            return ResponseEntity
                .ok(chargedUserPointBalance)
        }
        catch(ex: NegativeChargeAmountException){
            throw ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "포인트 충전은 0 이상의 정수만 가능합니다.")
        }
        catch (ex: UserNotFoundException){
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "회원이 존재하지 않습니다.")
        }
    }

    @PatchMapping("{id}/use")
    @Operation(summary = "포인트 사용", description = "특정 유저의 포인트를 사용합니다.")
    fun use(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
        @Parameter(description = "사용할 금액") 
        @RequestBody 
        amount: Long,
    ): ResponseEntity<UserPoint> {
        try{
            val usedUserPointBalance = pointService.usePoint(id, amount)

            return ResponseEntity
                .ok(usedUserPointBalance)
        }
        catch (ex: NegativeUsageException){
            throw ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "포인트 사용은 0 이상의 정수만 가능합니다.")
        }
        catch (ex: NotEnoughPointException){
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "사용할 수 있는 포인트가 부족합니다.")
        }
        catch (ex: UserNotFoundException){
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "회원이 존재하지 않습니다.")
        }
    }
}