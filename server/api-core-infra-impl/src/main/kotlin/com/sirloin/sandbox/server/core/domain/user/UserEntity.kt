package com.sirloin.sandbox.server.core.domain.user

import com.sirloin.sandbox.server.core.model.DateAuditable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

// 도메인 객체 생성에 여러 필드가 필요하기 때문에 불가피
@Suppress("LongParameterList")
@Table("users")
// POINT: 이 클래스를 data class 로 선언하고, equals / hashCode 를 삭제해도 문제가 없을까요?
// POINT: Entity 와 Domain Model 의 차이란 뭘까요?
data class UserEntity constructor(
    @get:Id
    var id: Long? = null,
    override val uuid: UUID,
    override var nickname: String,
    override var profileImageUrl: String,
    override var deletedAt: Instant?,
    @CreatedDate
    override var createdAt: Instant,
    @LastModifiedDate
    override var updatedAt: Instant,
    override var version: Long,
) : User.Editor {

    companion object {
        fun from(src: User): UserEntity = with(src) {
            UserEntity(
                id = if (this is UserEntity) id else null,
                uuid = uuid,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                deletedAt = deletedAt,
                createdAt = createdAt,
                updatedAt = updatedAt,
                version = version
            )
        }
    }
}
