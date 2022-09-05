package cn.sunline.saas.channel.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.rbac.modules.Permission

interface PermissionRepository : BaseRepository<Permission, Long>