package cn.sunline.saas.rbac.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(baseRepository: UserRepository) : BaseRepoService<User, Long>(baseRepository), UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun register(user: User): User {
        user.password = BCryptPasswordEncoder().encode(user.password)
        return save(user)
    }

    fun updateOne(oldUser: User, newUser: User): User {
        oldUser.roles = newUser.roles
        oldUser.email = newUser.email
        return save(oldUser)
    }

    fun validate(username: String, password: String): User? {
        val user = userRepository.findByUsername(username)?: return null
        return if (BCrypt.checkpw(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun getByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        return getByUsername(username)
    }
}