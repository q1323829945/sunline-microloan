package cn.sunline.saas.channel.services

import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.services.TOKEN_KEY_USERID
import cn.sunline.saas.channel.rbac.services.TOKEN_KEY_USERNAME
import cn.sunline.saas.channel.rbac.services.TokenService
import cn.sunline.saas.channel.rbac.services.UserService
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenServiceTest {
    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun `generate and validate token`(){
        val user = userService.register(
                User(
                    username = "testUserToken",
                    email = "test@test.test",
                    password = "test",
                    roles = mutableListOf()
                )
            )

        val token = tokenService.generateToken(user.id!!,user,user.jwtKey, DateTime.now().plusHours(1).toDate())

        Assertions.assertThat(token).isNotNull

        val map = tokenService.validateToken(user.username,token,user.jwtKey)


        Assertions.assertThat(map).isNotNull
        Assertions.assertThat(map?.get(TOKEN_KEY_USERNAME)).isEqualTo(user.username)
        Assertions.assertThat(map?.get(TOKEN_KEY_USERID).toString().toLong()).isEqualTo(user.id!!)
    }

}