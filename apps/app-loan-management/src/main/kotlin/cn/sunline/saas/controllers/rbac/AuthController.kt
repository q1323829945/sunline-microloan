package cn.sunline.saas.controllers.rbac

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.services.TokenService
import cn.sunline.saas.rbac.services.UserService
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
    data class DTOLoginResponse(val token: String)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    @PostMapping("login")
    fun login(@RequestBody dtoLogin: DTOLogin): ResponseEntity<DTOResponseSuccess<DTOLoginResponse>> {
        val user = userService.validate(dtoLogin.username, dtoLogin.password)?: throw ManagementException(ManagementExceptionCode.AUTHORIZATION_LOGIN_FAILED)
        val tokenExpire = DateTime.now().plusHours(1).toDate()
        val token = tokenService.generateToken(user.id!!, user, user.jwtKey, tokenExpire)
        return DTOResponseSuccess(DTOLoginResponse(token)).response()
    }

}