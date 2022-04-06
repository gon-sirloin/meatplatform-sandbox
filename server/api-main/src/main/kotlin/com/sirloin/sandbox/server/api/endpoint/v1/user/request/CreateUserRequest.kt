/*
 * sirloin-sandbox-server
 * Distributed under CC BY-NC-SA
 */
package com.sirloin.sandbox.server.api.endpoint.v1.user.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.sirloin.sandbox.server.api.validation.UnicodeCharsLength
import com.sirloin.sandbox.server.core.domain.user.User
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * @since 2022-02-14
 */
@JsonDeserialize
data class CreateUserRequest(
    @get:UnicodeCharsLength(
        min = User.NICKNAME_SIZE_MIN,
        max = User.NICKNAME_SIZE_MAX,
        message = "`nickname` must between ${User.NICKNAME_SIZE_MIN} and ${User.NICKNAME_SIZE_MAX} characters."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_PROFILE_IMAGE_URL)
    val profileImageUrl: String,

    @NotNull
    @get:Pattern(
        regexp="^[a-zA-Z0-9]*\$",
        message = "비밀번호에 특수문자를 포함 할 수 없습니다."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_PASSWORD)
    val password: String
) {
    companion object {
        const val DESC_NICKNAME = "${User.NICKNAME_SIZE_MIN}자 이상, ${User.NICKNAME_SIZE_MAX}자 이하의 이용자 닉네임."
        const val DESC_PROFILE_IMAGE_URL = "Profile image URL"
        const val DESC_PASSWORD = "비밀번호에 특수문자는 사용 할 수 없습니다."
    }
}
