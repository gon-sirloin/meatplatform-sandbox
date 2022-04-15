/*
 * sirloin-sandbox-server
 * Distributed under CC BY-NC-SA
 */
package testcase.small.domain.user

import com.sirloin.jvmlib.time.truncateToSeconds
import com.sirloin.sandbox.server.core.exception.ClientException
import com.sirloin.sandbox.server.core.i18n.LocaleProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import test.com.sirloin.annotation.SmallTest
import test.small.domain.randomUser
import java.time.Instant

@SmallTest
class UserSpec {
    @DisplayName("생성한 User 를 삭제하면 삭제 시점의 시간이 기록된다")
    @Test
    fun `Timestamp is saved when deleting a user`() {
        // given:
        val user = randomUser()

        // when:
        val beforeDelete = Instant.now().truncateToSeconds()
        val deletedUser = user.edit().delete()

        // then:
        assertAll(
            { assertThat(deletedUser.isDeleted, `is`(true)) },
            { assertThat(deletedUser.deletedAt, greaterThanOrEqualTo(beforeDelete)) }
        )
    }

    @DisplayName("패스워드 인증 검사 테스트")
    @Test
    fun `password valid false`() {
        // given:
        val user = randomUser()
        val wrongPassword = user.password+1

        // when: then:
        assertThrows<ClientException> {
            (user.assertPassword(wrongPassword, LocaleProvider.defaultInstance()))
        }

    }
}
