/*
 * sirloin-sandbox-server
 * Distributed under CC BY-NC-SA
 */
package testcase.large.domain.user

import com.sirloin.jvmlib.time.truncateToSeconds
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.both
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.lessThan
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.isNotNull
import test.small.domain.betweenTwoSeconds
import test.small.domain.randomUser
import java.time.Instant

class SaveAndFindUserTest : UserRepositoryLargeTestBase() {
    @DisplayName("저장한 Entity model 을 검색할 수 있다")
    @Test
    fun `Should find by query after entity model is saved`() {
        // given:
        val now = Instant.now().truncateToSeconds()
        val user = randomUser(createdAt = now, updatedAt = now)

        // when:
        val savedUser = sut.save(user)

        // then:
        val foundUser = sut.findByUuid(savedUser.uuid)

        // expect:
        assertThat(foundUser, `is`(savedUser))
    }

    @DisplayName("Entity model을 저장시 createdAt과 updateAt이 현재시간으로 입력된다.")
    @Test
    fun `save the current time information of createAt is save`() {
        // given:
        val minusOneDay = Instant.now().minusSeconds(86400).truncateToSeconds()
        val user = randomUser(createdAt = minusOneDay, updatedAt = minusOneDay)

        // when:
        val savedUser = sut.save(user)

        // then:
        val foundUser = sut.findByUuid(savedUser.uuid)

        // expect:
        assertAll(
            { assertThat(foundUser, `is`(savedUser))},
            { assertThat(foundUser?.createdAt, `is`(betweenTwoSeconds())) },
            { assertThat(foundUser?.updatedAt, `is`(betweenTwoSeconds())) }
        )
    }



}
