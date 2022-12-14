package cn.sunline.saas.channel.rbac.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.channel.rbac.exception.PersonAlreadyBindingException
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.repositories.UserRepository
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(baseRepository: UserRepository) : BaseMultiTenantRepoService<User, Long>(baseRepository), UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun register(user: User): User {
        user.password = BCryptPasswordEncoder().encode(user.password)

        user.personId?.run {
            findByPersonId(this)?.run {
                throw PersonAlreadyBindingException("person already binding")
            }
        }

        return save(user)
    }

    fun updateOne(oldUser: User, newUser: User): User {
        newUser.personId?.run {
            if(this != oldUser.personId){
                findByPersonId(this)?.run {
                    throw PersonAlreadyBindingException("person already binding")
                }
            }
        }

        oldUser.roles = newUser.roles
        oldUser.email = newUser.email
        oldUser.personId = newUser.personId
        oldUser.position = newUser.position

        return save(oldUser)
    }

    fun validate(username: String, password: String): User? {
        val user = userRepository.findByUsernameAndTenantId(username, ContextUtil.getTenant().toLong())?: return null
        return if (BCrypt.checkpw(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun getByUsername(username: String): User? {
        return userRepository.findByUsernameAndTenantId(username, ContextUtil.getTenant().toLong())
    }

    private fun findByPersonId(personId:Long):User?{
        return userRepository.findByPersonIdAndTenantId(personId, ContextUtil.getTenant().toLong())
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        return getByUsername(username)
    }
}