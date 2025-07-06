package io.hhplus.tdd.point

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
@Tag(name = "Point API", description = "포인트 관리 API")
class PointController {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("{id}")
    @Operation(summary = "포인트 조회", description = "특정 유저의 포인트를 조회합니다.")
    fun point(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
    ): UserPoint {
        return UserPoint(id, 1000L, System.currentTimeMillis())
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    @Operation(summary = "포인트 내역 조회", description = "특정 유저의 포인트 충전/사용 내역을 조회합니다.")
    fun history(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
    ): List<PointHistory> {
        return emptyList()
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    @Operation(summary = "포인트 충전", description = "특정 유저의 포인트를 충전합니다.")
    fun charge(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
        @Parameter(description = "충전할 금액") 
        @RequestBody 
        amount: Long,
    ): UserPoint {
        return UserPoint(0, 0, 0)
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    @Operation(summary = "포인트 사용", description = "특정 유저의 포인트를 사용합니다.")
    fun use(
        @Parameter(description = "사용자 ID") 
        @PathVariable 
        id: Long,
        @Parameter(description = "사용할 금액") 
        @RequestBody 
        amount: Long,
    ): UserPoint {
        return UserPoint(0, 0, 0)
    }
}