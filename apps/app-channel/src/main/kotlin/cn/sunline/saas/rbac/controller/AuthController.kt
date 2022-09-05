package cn.sunline.saas.rbac.controller

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.exception.AuthBusinessException
import cn.sunline.saas.channel.rbac.services.TokenService
import cn.sunline.saas.channel.rbac.services.UserService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
class AuthController {
    data class DTOLogin(val username: String, val password: String)
    data class DTOLoginResponse(val token: String,val username: String,val email: String)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    @PostMapping("login")
    fun login(@RequestBody dtoLogin: DTOLogin): ResponseEntity<DTOResponseSuccess<DTOLoginResponse>> {
        val user = userService.validate(dtoLogin.username, dtoLogin.password)?: throw AuthBusinessException("Login fail",ManagementExceptionCode.AUTHORIZATION_LOGIN_FAILED)
        val tokenExpire = DateTime.now().plusHours(1).toDate()
        val token = tokenService.generateToken(user.id!!, user, user.jwtKey, tokenExpire)
        val username = user.username
        val email = user.email
        return DTOResponseSuccess(DTOLoginResponse(token,username,email)).response()
    }

}