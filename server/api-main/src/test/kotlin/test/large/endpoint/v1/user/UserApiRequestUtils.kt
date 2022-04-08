/*
 * sirloin-sandbox-server
 * Distributed under CC BY-NC-SA
 */
package test.large.endpoint.v1.user

import com.github.javafaker.Faker
import com.sirloin.sandbox.server.api.endpoint.v1.user.request.CreateUserRequest
import com.sirloin.sandbox.server.api.endpoint.v1.user.request.UpdateUserRequest

private const val DEFAULT_STRING = "__DEFAULT__"
private const val DEFAULT_PASSWORD = "PASSWORD1234"

fun CreateUserRequest.Companion.random(
    nickname: String? = null,
    profileImageUrl: String? = null,
    password:String? = null,
): CreateUserRequest = with(Faker()) {
    return CreateUserRequest(
        nickname = nickname ?: name().username(),
        profileImageUrl = profileImageUrl ?: internet().image(),
        password =  password ?: internet().password()
    )
}

fun UpdateUserRequest.Companion.random(
    nickname: String? = DEFAULT_STRING,
    profileImageUrl: String? = DEFAULT_STRING,
    password: String = DEFAULT_PASSWORD
): UpdateUserRequest = with(Faker()) {
    return UpdateUserRequest(
        nickname = if (nickname == DEFAULT_STRING) {
            name().username()
        } else {
            nickname
        },
        profileImageUrl = if (profileImageUrl == DEFAULT_STRING) {
            internet().image()
        } else {
            profileImageUrl
        },
        password = if (password == DEFAULT_PASSWORD) {
            internet().password()
        } else {
            password
        }

    )
}
