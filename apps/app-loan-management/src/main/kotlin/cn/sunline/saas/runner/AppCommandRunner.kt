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
import org.springframework.stereotype.Service

@Service
class AppCommandRunner(
        val permissionService: PermissionService,
        val roleService: RoleService,
        val userService: UserService
){
    private val adminUsername = "admin"
    private val adminRole = "ROLE_ADMIN"

    fun run() {
        reloadPermissions()
        reloadAdminRole()
        reloadAdminUser()
    }

    private fun reloadAdminUser() {
        val adminUser = userService.getByUsername(adminUsername)?: kotlin.run {
            val newUser = User(username = adminUsername, password = adminUsername, email = "admin@sunline.cn")
            userService.register(newUser)
        }

        roleService.getByName(adminRole)?.run {
            adminUser.roles.clear()
            adminUser.roles.add(this)
            userService.save(adminUser)
        }
    }

    private fun reloadAdminRole() {
        val adminRole = roleService.getByName(adminRole)?: kotlin.run {
            Role(name = adminRole, remark = "Admin Role")
        }

        val permissions = permissionService.getPageWithTenant(pageable = Pageable.unpaged()).toSet()
        adminRole.permissions.clear()
        adminRole.permissions.addAll(permissions)
        roleService.save(adminRole)
    }

    private fun reloadPermissions() {
        val allPermissionConfigs = PermissionConfig.values().map { it.name }.toSet()
        val existingPermissionConfigs = permissionService.getPageWithTenant(pageable = Pageable.unpaged()).map { it.name }.toSet()
        val missingPermissionConfigs = allPermissionConfigs
                .filter { !existingPermissionConfigs.contains(it) && validatePermissionConfig(it) }
                .map { PermissionConfig.valueOf(it) }
                .map { Permission(name = it.name, tag = it.permissionGroup, remark = it.remark) }
                .toList()
        permissionService.save(missingPermissionConfigs)
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