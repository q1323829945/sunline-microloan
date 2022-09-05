package cn.sunline.saas.channel.services

import cn.sunline.saas.channel.rbac.modules.Permission
import cn.sunline.saas.channel.rbac.modules.Role
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.services.PermissionService
import cn.sunline.saas.channel.rbac.services.RoleService
import cn.sunline.saas.channel.rbac.services.UserService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RbacServiceTest {
    @Autowired
    private lateinit var permissionService:PermissionService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var userService: UserService

    @BeforeAll
    fun `init`(){
        val permission = permissionService.save(Permission(
            id = null,
            tag = "testPermission",
            name = "TEST_GET",
            remark = "test"
        ))

        val role = roleService.save(Role(
            id = null,
            name = "testRole",
            remark = "test",
            permissions = mutableListOf(permission)
        ))

        userService.register(User(
            username = "testUser",
            email = "test@test.test",
            password = "test",
            roles = mutableListOf(role)
        ))
    }


    @Test
    fun `get permission`(){
        val permission = permissionService.getOne(1)

        Assertions.assertThat(permission).isNotNull
    }

    @Test
    fun `get role by name`(){
        val role = roleService.getByName("testRole")

        Assertions.assertThat(role).isNotNull
    }

    @Test
    fun `update role`(){
        val oldOne = roleService.getByName("testRole")!!
        val newPermission = permissionService.save(Permission(
            id = null,
            tag = "testPermission2",
            name = "TEST_GET2",
            remark = "test2"
        ))
        val newOne = Role(null,"testRole","", mutableListOf(newPermission))
        val role = roleService.updateOne(oldOne,newOne)

        Assertions.assertThat(role.remark).isEqualTo("")
        Assertions.assertThat(role.permissions[0].name).isEqualTo("TEST_GET2")
    }

    @Test
    fun `get user by username`(){
        val user = userService.getByUsername("testUser")

        Assertions.assertThat(user).isNotNull
    }

    @Test
    fun `update user`(){
        val oldUser = userService.getByUsername("testUser")!!

        val newRole = roleService.save(Role(
            id = null,
            name = "testRole2",
            remark = "",
        ))

        val newUser = User(
            username = "testUser",
            email = "test2@test.test",
            roles = mutableListOf(newRole)
        )

        val user = userService.updateOne(oldUser,newUser)

        Assertions.assertThat(user.email).isEqualTo("test2@test.test")
        Assertions.assertThat(user.roles[0].name).isEqualTo("testRole2")
    }

}