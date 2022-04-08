package cn.sunline.saas.runner

import cn.sunline.saas.config.PermissionConfig
import cn.sunline.saas.rbac.modules.Permission
import cn.sunline.saas.rbac.modules.Role
import cn.sunline.saas.rbac.modules.User
import cn.sunline.saas.rbac.services.PermissionService
import cn.sunline.saas.rbac.services.RoleService
import cn.sunline.saas.rbac.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class AppCommandRunner(
        val permissionService: PermissionService,
        val roleService: RoleService,
        val userService: UserService
) : CommandLineRunner {
    private val adminUsername = "admin"
    private val adminRole = "ROLE_ADMIN"

    override fun run(vararg args: String?) {
        reloadPermissions()
        reloadAdminRole()
        reloadAdminUser()
    }

    private fun reloadAdminUser() {
        val adminUser = userService.getByUsername(adminUsername)?: kotlin.run {
            println("Admin account not found, creating a new admin account")
            val newUser = User(username = adminUsername, password = adminUsername, email = "admin@sunline.cn")
            userService.register(newUser)
        }

        roleService.getByName(adminRole)?.run {
            adminUser.roles.clear()
            adminUser.roles.add(this)
            userService.save(adminUser)
            println("Admin role has been assigned to Admin account")
        }
    }

    private fun reloadAdminRole() {
        val adminRole = roleService.getByName(adminRole)?: kotlin.run {
            println("Admin role not found, creating a new admin role")
            Role(name = adminRole, remark = "Admin Role")
        }

        val permissions = permissionService.getPaged(pageable = Pageable.unpaged()).toSet()

        println("Adding total of ${permissions.size} permission configs to admin role")
        adminRole.permissions.clear()
        adminRole.permissions.addAll(permissions)
        val addedAdminRole = roleService.save(adminRole)
        println("Added permission configs for admin role")
    }

    private fun reloadPermissions() {
        val allPermissionConfigs = PermissionConfig.values().map { it.name }.toSet()
        val existingPermissionConfigs = permissionService.getPaged(pageable = Pageable.unpaged()).map { it.name }.toSet()
        val missingPermissionConfigs = allPermissionConfigs
                .filter { !existingPermissionConfigs.contains(it) && validatePermissionConfig(it) }
                .map { PermissionConfig.valueOf(it) }
                .map { Permission(name = it.name, tag = it.permissionGroup, remark = it.remark) }
                .toList()

        println("Adding total of ${missingPermissionConfigs.size} permission configs")

        val addedPermissionConfigs = permissionService.save(missingPermissionConfigs)
        println("Added permission configs: $addedPermissionConfigs")
    }

    private fun validatePermissionConfig(name: String): Boolean {
        return try {
            PermissionConfig.valueOf(name)
            true
        } catch (ex: IllegalArgumentException) {
            false
        }
    }
}