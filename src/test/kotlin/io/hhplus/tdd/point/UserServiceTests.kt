package io.hhplus.tdd.point

import io.kotest.assertions.fail
import io.kotest.core.spec.style.DescribeSpec

/**
 * 인터넷에서 퍼온거 내 입맛대로 템플릿 대충 구조화 해봄
 *
 * 출처: https://kth990303.tistory.com/362
 *
 */
class UserServiceTests : DescribeSpec({

    // given (~~ 일 때)
    describe("비밀번호를 변경할 때") {

        // when (~~ 하면)
        context("확인용 비밀번호가 일치한다면") {

            // then (~~ 이다.)
            it("비밀번호가 성공적으로 변경된다.") {
                // ...
                fail("ㅋㅋ 어쩔")
            }

            it("기존 로그인 세션이 만료 처리된다.") {
                fail("ㅋㅋ 어쩔")
            }
        }

        // when
        context("확인용 비밀번호가 일치하지 않으면"){

            // then
            it("비밀번호 불일치 예외가 발생한다.") {
                // ...
                fail("ㅋㅋ 어쩔")
            }
        }
    }
})

