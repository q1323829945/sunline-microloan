package cn.sunline.saas.runner

import cn.sunline.saas.config.PermissionConfig
import cn.sunline.saas.global.constant.PositionType
import cn.sunline.saas.channel.rbac.modules.Permission
import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.Role
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.services.PermissionService
import cn.sunline.saas.channel.rbac.services.PositionService
import cn.sunline.saas.channel.rbac.services.RoleService
import cn.sunline.saas.channel.rbac.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class AppCommandRunner(
        val permissionService: PermissionService,
        val roleService: RoleService,
        val userService: UserService,
        val positionService:PositionService
) : CommandLineRunner {
    private val adminUsername = "admin"
    private val adminRole = "ROLE_ADMIN"

    override fun run(vararg args: String?) {
        reloadPermissions()
        reloadAdminRole()
        val positions = reloadPosition()
        reloadAdminUser(positions)
    }

    private fun reloadPosition():Map<String,Position>{
        val positionMap = mutableMapOf<String,Position>()

        PositionType.values().forEach {
            val position = positionService.getOne(it.position)?:run{
                val newPosition = Position(id = it.position, name = it.position, remark = "default position")
                positionService.save(newPosition)
            }
            positionMap[it.position] = position
        }
        return positionMap
    }

    private fun reloadAdminUser(positionMap:Map<String,Position>) {
        val adminUser = userService.getByUsername(adminUsername)?: kotlin.run {
            val newUser = User(username = adminUsername, password = adminUsername, email = "admin@sunline.cn",position = positionMap["admin"]!!)
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

        val permissions = permissionService.getPaged(pageable = Pageable.unpaged()).toSet()
        adminRole.permissions.clear()
        adminRole.permissions.addAll(permissions)
        roleService.save(adminRole)
    }

    private fun reloadPermissions() {
        val allPermissionConfigs = PermissionConfig.values().map { it.name }.toSet()
        val existingPermissionConfigs = permissionService.getPaged(pageable = Pageable.unpaged()).map { it.name }.toSet()
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