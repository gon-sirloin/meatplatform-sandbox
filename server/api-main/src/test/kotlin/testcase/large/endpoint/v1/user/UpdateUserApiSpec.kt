/*
 * sirloin-sandbox-server
 * Distributed under CC BY-NC-SA
 */
package testcase.large.endpoint.v1.user

import com.sirloin.sandbox.server.api.endpoint.v1.ApiPathsV1
import com.sirloin.sandbox.server.api.endpoint.v1.user.request.UpdateUserRequest
import com.sirloin.sandbox.server.api.endpoint.v1.user.response.UserResponse
import com.sirloin.sandbox.server.core.exception.MtExceptionCode
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpStatus
import test.large.endpoint.v1.user.createRandomUser
import test.large.endpoint.v1.user.random
import test.large.endpoint.v1.user.updateUserRequestFieldsSnippet
import test.large.endpoint.v1.user.userInfoResponseFieldsSnippet
import testcase.large.endpoint.v1.UserTestBaseV1
import java.util.*
import java.util.stream.Stream

class UpdateUserApiSpec : UserTestBaseV1() {
    @DisplayName("입력한 값을 이용해 이용자 정보를 변경한다")
    @Test
    fun userInfoUpdated() {
        // given:
        val createdUser = createRandomUser()

        // when:
        val request = UpdateUserRequest.random(password = createdUser.password!!)

        // then:
        val response = jsonRequest()
            .withDocumentation(updateUserRequestFieldsSnippet(), userInfoResponseFieldsSnippet())
            .body(request)
            .patch(ApiPathsV1.userWithUuid(createdUser.uuid))
            .expect2xx(UserResponse::class)

        // expect:
        assertAll(
            { assertThat(response.nickname, `is`(request.nickname)) },
            { assertThat(response.profileImageUrl, `is`(request.profileImageUrl)) }
        )
    }

    @ParameterizedTest(name = "생략한 필드는 업데이트 되지 않는다")
    @MethodSource("omittedFieldNotUpdatedParams")
    fun omittedFieldNotUpdated(request: UpdateUserRequest) {
        // given:
        val createdUser = createRandomUser()



        // then:
        val response = jsonRequest()
            .withDocumentation(updateUserRequestFieldsSnippet(), userInfoResponseFieldsSnippet())
            .body(request.copy(password = createdUser.password!!))
            .patch(ApiPathsV1.userWithUuid(createdUser.uuid))
            .expect2xx(UserResponse::class)

        // expect:
        assertAll(
            { request.nickname?.let { assertThat(response.nickname, `is`(it)) } },
            { request.profileImageUrl?.let { assertThat(response.profileImageUrl, `is`(it)) } }
        )
    }

    @DisplayName("없는 이용자의 정보를 업데이트 할 수 없다 (HTTP 404)")
    @Test
    fun errorByNotExistingUser() {
        // when:
        val request = UpdateUserRequest.random()

        // then:
        jsonRequest()
            .withErrorDocumentation()
            .body(request)
            .patch(ApiPathsV1.userWithUuid(UUID.randomUUID()))
            .expect4xx(HttpStatus.NOT_FOUND)
            .withExceptionCode(MtExceptionCode.USER_NOT_FOUND)
    }

    @DisplayName("비밀번호 일치시 업데이트가 된다.")
    @Test
    fun updateInfoByPasswordVerifySuccess() {
        // given:
        val createdUser = createRandomUser()

        // when:
        val request = UpdateUserRequest.random(password = createdUser.password!!)

        // then:
        jsonRequest()
            .withDocumentation(updateUserRequestFieldsSnippet(), userInfoResponseFieldsSnippet())
            .body(request)
            .patch(ApiPathsV1.userWithUuid(createdUser.uuid))
            .expect2xx(UserResponse::class)
    }

    @DisplayName("비밀번호 불일치시 예외 발생한다.")
    @Test
    fun notUpdateInfoByPasswordVerifyFail() {
        // given:
        val createdUser = createRandomUser()

        // when:
        val request = UpdateUserRequest.random(password = (createdUser.password!! + "1"))

        // then:
        jsonRequest()
//            .withDocumentation(updateUserRequestFieldsSnippet(), userInfoResponseFieldsSnippet())
            .body(request)
            .patch(ApiPathsV1.userWithUuid(createdUser.uuid))
            .expect4xx(HttpStatus.UNAUTHORIZED)
            .withExceptionCode(MtExceptionCode.WRONG_PASSWORD)


    }


    companion object {
        @JvmStatic
        fun omittedFieldNotUpdatedParams(): Stream<UpdateUserRequest> = Stream.of(
            UpdateUserRequest.random(nickname = null),
            UpdateUserRequest.random(profileImageUrl = null)
        )
    }
}
